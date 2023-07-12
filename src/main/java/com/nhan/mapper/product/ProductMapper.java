package com.nhan.mapper.product;

import com.nhan.model.dto.product.ProductBasicDTO;
import com.nhan.model.dto.product.ProductCreateDTO;
import com.nhan.model.dto.product.ProductDetailDTO;
import com.nhan.model.entity.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromCreateToEntity(ProductCreateDTO productCreateDTO);

    ProductBasicDTO fromEntityToBasic(Product product);

    ProductDetailDTO fromEntityToDetail(Product product);

}
