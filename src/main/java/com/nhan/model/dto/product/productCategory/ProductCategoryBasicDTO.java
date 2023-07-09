package com.nhan.model.dto.product.productCategory;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductCategoryBasicDTO {
    private UUID id;

    private String name;

    private String code;

    private UUID parentId;
}
