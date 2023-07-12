package com.nhan.model.dto.product.productBrand;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductBrandDetailDTO {

    private UUID id;

    private String name;

    private String code;

}
