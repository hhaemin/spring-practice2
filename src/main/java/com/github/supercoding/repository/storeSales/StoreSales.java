package com.github.supercoding.repository.storeSales;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@ToString
@Builder
public class StoreSales {
    private Integer id;
    private String storeName;
    private Integer amount;

}
