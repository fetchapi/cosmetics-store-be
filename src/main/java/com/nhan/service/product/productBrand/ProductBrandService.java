package com.nhan.service.product.productBrand;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.product.productBrand.ProductBrandCreateDTO;
import com.nhan.model.dto.product.productBrand.ProductBrandUpdateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;

import java.util.UUID;

public interface ProductBrandService {

    ResponseModelDTO createNewProductBrand(ProductBrandCreateDTO productBrandCreateDTO) throws NotFoundException;

    ResponseModelDTO updateById(UUID id, ProductBrandUpdateDTO productBrandUpdateDTO) throws NotFoundException;

    ResponseModelDTO findAll();

    ResponseModelDTO findById(UUID id) throws NotFoundException;

    void softDeleteById(UUID id) throws NotFoundException;

}
