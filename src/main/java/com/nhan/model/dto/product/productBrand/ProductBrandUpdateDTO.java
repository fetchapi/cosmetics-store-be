package com.nhan.model.dto.product.productBrand;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductBrandUpdateDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "code is required")
    private String code;

}
