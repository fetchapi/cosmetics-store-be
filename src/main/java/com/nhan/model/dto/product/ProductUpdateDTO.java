package com.nhan.model.dto.product;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ProductUpdateDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "price is required")
    private Long price;

    @NotNull(message = "discount is required")
    private int discount;

    @NotNull(message = "quantity is required")
    private int quantity;

    @NotNull(message = "productCategoryId is required")
    private UUID productCategoryId;

    @NotNull(message = "productBrandId is required")
    private UUID productBrandId;

}
