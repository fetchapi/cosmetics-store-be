package com.nhan.service.product.productCategory;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.product.productCategory.ProductCategoryCreateDTO;
import com.nhan.model.dto.product.productCategory.ProductCategoryUpdateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;

import java.util.UUID;

public interface ProductCategoryService {

    ResponseModelDTO createNewProductCategory(ProductCategoryCreateDTO productCategoryCreateDTO) throws NotFoundException;

    ResponseModelDTO updateById(UUID id, ProductCategoryUpdateDTO productCategoryUpdateDTO) throws NotFoundException;

    ResponseModelDTO findAll();

    ResponseModelDTO findById(UUID id) throws NotFoundException;

    void softDeleteById(UUID id) throws NotFoundException;

}
