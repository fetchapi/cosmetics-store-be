package com.nhan.mapper.product;

import com.nhan.model.dto.product.*;
import com.nhan.model.entity.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromCreateToEntity(ProductCreateDTO productCreateDTO);

    ProductBasicDTO fromEntityToBasic(Product product);

    ProductDetailDTO fromEntityToDetail(Product product);

    ProductFilterBasicDTO fromEntityToFilterBasic(Product product);

    ProductFilterDetailDTO fromEntityToFilterDetail(Product product);

}
