package com.nhan.mapper.product;

import com.nhan.model.dto.product.productCategory.ProductCategoryCreateDTO;
import com.nhan.model.dto.product.productCategory.ProductCategoryBasicDTO;
import com.nhan.model.entity.product.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    ProductCategory fromCreateToEntity(ProductCategoryCreateDTO productCategoryCreateDTO );

    ProductCategoryBasicDTO fromEntityToBasic(ProductCategory productCategory);

}
