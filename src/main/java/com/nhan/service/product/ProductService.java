package com.nhan.service.product;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.product.ProductCreateDTO;
import com.nhan.model.dto.product.ProductUpdateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.response.ResponsePageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ResponseModelDTO createNewProduct(ProductCreateDTO productCreateDTO, MultipartFile image) throws NotFoundException;

    ResponsePageDTO findAll(String keyword, Pageable pageable);

    ResponsePageDTO filterByCategoryList(List<UUID> uuids, Pageable pageable);

    ResponsePageDTO searchProduct(String keyword, Pageable pageable);

    ResponseModelDTO findById(UUID id) throws NotFoundException;

    ResponseModelDTO updateById(UUID id, ProductUpdateDTO productUpdateDTO) throws NotFoundException;

    void softDeleteById(UUID id) throws NotFoundException;
}
