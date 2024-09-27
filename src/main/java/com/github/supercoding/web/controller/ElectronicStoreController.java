package com.github.supercoding.web.controller;

import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.items.BuyOrder;
import com.github.supercoding.web.dto.items.Item;
import com.github.supercoding.web.dto.items.ItemBody;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ElectronicStoreController {

    private ElectronicStoreItemService electronicStoreItemService;

    public ElectronicStoreController(ElectronicStoreItemService electronicStoreItemService) {
        this.electronicStoreItemService = electronicStoreItemService;
    }

    private static int serialItemId = 1;
    private List<Item> items = Arrays.asList(
            new Item(String.valueOf(serialItemId++), "Apple iPhone 12 Pro Max", "Smartphone", 1490000, "A14 Bionic", "512GB"),
            new Item(String.valueOf(serialItemId++), "Samsung Galaxy S21 Ultra", "Smartphone", 1690000, "Exynos 2100", "256GB"),
            new Item(String.valueOf(serialItemId++), "Google Pixel 6 Pro", "Smartphone", 1290000, "Google Tensor", "128GB"),
            new Item(String.valueOf(serialItemId++), "Dell XPS 15", "Laptop", 2290000, "Intel Core i9", "1TB SSD"),
            new Item(String.valueOf(serialItemId++), "Sony Alpha 7 III", "Mirrorless Camera", 2590000, "BIONZ X", "No internal storage"),
            new Item(String.valueOf(serialItemId++), "Microsoft Xbox Series X", "Gaming Console", 499000, "Custom AMD Zen 2", "1TB SSD"));

    @GetMapping("/items")
    public List<Item> findAllItem(){
        return electronicStoreItemService.findAllItem();
    }

    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody){
        Integer itemId = electronicStoreItemService.saveItem(itemBody);
        return "ID: " + itemId;
    }

    @GetMapping("/items/{id}")
    public Item findItemByPathId(@PathVariable String id){
        return electronicStoreItemService.findItemByInt(id);
    }

    @GetMapping("/items-query")
    public Item findItemByQueryId(@RequestParam("id") String id){
        return electronicStoreItemService.findItemByInt(id);
    }

    @GetMapping("/items-queries")
    public List<Item> findItemByQueryIds(@RequestParam("id") List<String> ids){
        return electronicStoreItemService.findItemsByIds(ids);
    }

    @DeleteMapping("/items/{id}")
    public String deleteItemByPathId(@PathVariable String id){
        electronicStoreItemService.deleteItem(id);
        return "Object with id = " + id + " has been deleted";
    }

    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable String id, @RequestBody ItemBody itemBody){
        return electronicStoreItemService.updateItem(id,itemBody);
    }

    @PostMapping("/items/buy")
    public String buyItem(@RequestBody BuyOrder buyOrder){

        Integer orderItemNums = electronicStoreItemService.buyItems(buyOrder);
        return "요청하신 Item 중 " + orderItemNums + "개를 구매하였습니다.";
    }
}
