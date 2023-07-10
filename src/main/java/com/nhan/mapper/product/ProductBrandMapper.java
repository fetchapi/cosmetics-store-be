package com.nhan.mapper.product;

import com.nhan.model.dto.product.productBrand.ProductBrandCreateDTO;
import com.nhan.model.dto.product.productBrand.ProductBrandDetailDTO;
import com.nhan.model.entity.product.ProductBrand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductBrandMapper {

    ProductBrand fromCreateToEntity(ProductBrandCreateDTO productBrandCreateDTO);

    ProductBrandDetailDTO fromEntityToDetail(ProductBrand productBrand);

}
