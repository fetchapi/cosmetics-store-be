package com.nhan.model.dto.product;

import com.nhan.model.dto.product.productBrand.ProductBrandDetailDTO;
import com.nhan.model.dto.product.productCategory.ProductCategoryBasicDTO;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductFilterDetailDTO {

    private UUID id;

    private String name;

    private String slug;

    private Long price;

    private int discount;

    private int quantity;

    private String imageUrl;

    private ProductCategoryBasicDTO productCategory;

    private ProductBrandDetailDTO productBrand;

}
