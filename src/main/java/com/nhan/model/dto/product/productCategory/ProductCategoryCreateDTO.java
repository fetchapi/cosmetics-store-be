package com.nhan.model.dto.product.productCategory;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class ProductCategoryCreateDTO {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "code is required")
    private String code;

    private UUID parentId;

}
