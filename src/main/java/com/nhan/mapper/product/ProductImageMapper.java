package com.nhan.mapper.product;

import com.nhan.model.dto.product.productImage.ProductImageBasicDTO;
import com.nhan.model.entity.product.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    ProductImageBasicDTO fromEntityToBasic(ProductImage productImage);

}
