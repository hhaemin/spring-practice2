package com.github.supercoding.web.dto.items;

import com.github.supercoding.repository.Items.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Item {
    @Schema(name = "id", description = "Item Id", example = "1") private String id;
    @Schema(name = "name", description = "Item 이름", example = "Dell XPS 15") private String name;
    @Schema(name = "type", description = "Item 기기타입", example = "Laptop") private String type;
    @Schema(name = "price", description = "Item 가격", example = "1250000") private Integer price;
    private Spec spec;
}
