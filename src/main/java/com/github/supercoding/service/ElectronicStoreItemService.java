package com.github.supercoding.service;

import com.github.supercoding.repository.Items.ElectronicStoreItemJpaRepository;
import com.github.supercoding.repository.Items.ElectronicStoreItemRepository;
import com.github.supercoding.repository.Items.ItemEntity;
import com.github.supercoding.repository.storeSales.StoreSales;
import com.github.supercoding.repository.storeSales.StoreSalesJpaRepository;
import com.github.supercoding.repository.storeSales.StoreSalesRepository;
import com.github.supercoding.web.dto.items.BuyOrder;
import com.github.supercoding.web.dto.items.Item;
import com.github.supercoding.web.dto.items.ItemBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreItemService {

    private final ElectronicStoreItemRepository electronicStoreItemRepository;
    private final ElectronicStoreItemJpaRepository electronicStoreItemJpaRepository;

    private final StoreSalesRepository storeSalesRepository;
    private final StoreSalesJpaRepository storeSalesJpaRepository;

    public List<Item> findAllItem() {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();
        return itemEntities.stream().map(Item::new).collect(Collectors.toList());
    }

    public Integer saveItem(ItemBody itemBody) {
        ItemEntity itemEntity = new ItemEntity(null, itemBody.getName(), itemBody.getType(),
                itemBody.getPrice(), itemBody.getSpec().getCpu(), itemBody.getSpec().getCapacity());

        return electronicStoreItemRepository.saveItem(itemEntity);
    }

    public Item findItemByInt(String id) {
        Integer idInt = Integer.parseInt(id);
        ItemEntity itemEntity = electronicStoreItemRepository.findItemById(idInt);
        Item item = new Item(itemEntity);
        return item;
    }

    public List<Item> findItemsByIds(List<String> ids) {
        List<ItemEntity> itemEntities = electronicStoreItemRepository.findAllItems();
        return itemEntities.stream()
                .map(Item::new)
                .filter((item -> ids.contains(item.getId())))
                .collect(Collectors.toList());
    }

    public void deleteItem(String id) {
        Integer idInt = Integer.parseInt(id);
        electronicStoreItemRepository.deleteItem(idInt);
    }

    public Item updateItem(String id, ItemBody itemBody) {
        Integer idInt = Integer.valueOf(id);
        ItemEntity itemEntity = new ItemEntity(idInt, itemBody.getName(), itemBody.getType(),
                itemBody.getPrice(), itemBody.getSpec().getCpu(), itemBody.getSpec().getCapacity());

        ItemEntity itemEntityUpdated = electronicStoreItemRepository.updateItemEntity(idInt, itemEntity);

        return new Item(itemEntityUpdated);
    }

    @Transactional(transactionManager = "tm1")
    public Integer buyItems(BuyOrder buyOrder) {
        // 1. BuyOrder 에서 상품 ID와 수량을 알아낸다.
        // 2. 상품을 조회하여 수량이 얼마나 있는지 확인한다.
        // 3. 상품의 수량과 가격을 가지고 계산하여 총 가격을 구한다.
        // 4. 상품의 재고에 기존 계산한 재고를 구매하는 수량을 뺀다.
        // 5. 상품 구매하는 수량 * 가격 만큼 가계 매상으로 올린다.
        // (단, 재고가 아예 없거나 매장을 찾을 수 없으면 살 수 없다.)

        Integer itemId = buyOrder.getItemId();
        Integer itemNums = buyOrder.getItemNums();

        ItemEntity itemEntity = electronicStoreItemRepository.findItemById(itemId);

        if(itemEntity.getStoreId() == null) throw new RuntimeException("매장을 찾을 수 없습니다.");
        if(itemEntity.getStock() <= 0) throw new RuntimeException("상품의 재고가 없습니다.");

        Integer successBuyItemNums;
        if(itemNums >= itemEntity.getStock() ) successBuyItemNums = itemEntity.getStock();
        else successBuyItemNums = itemNums;

        Integer totalPrice = successBuyItemNums * itemEntity.getPrice();

        // Item 재고 감소
        electronicStoreItemRepository.updateItemStock(itemId, itemEntity.getStock() - successBuyItemNums);

        if(successBuyItemNums == 4) {
            log.error("4개를 구매하는건 허락하지 않습니다.");
            throw new RuntimeException("4개를 구매하는건 허락하지 않습니다.");
        }

        // 매장 매상 추가
        StoreSales storeSales = storeSalesRepository.findStoreSalesById(itemEntity.getStoreId());
        storeSalesRepository.updateSalesAmount(itemEntity.getStoreId(), storeSales.getAmount() + totalPrice );

        return successBuyItemNums;
    }

    public List<Item> findItemsByTypes(List<String> types) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByTypeIn(types);
        return itemEntities.stream().map(Item::new).collect(Collectors.toList());
    }

    public List<Item> findItemsOrderByPrices(Integer maxValue) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByPriceLessThanEqualOrderByPriceAsc(maxValue);
        return itemEntities.stream().map(Item::new).collect(Collectors.toList());
    }
}
