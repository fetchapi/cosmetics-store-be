package com.nhan.service.product.productImage;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.response.ResponseModelDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {

    ResponseModelDTO uploadImages(UUID productId, List<MultipartFile> fileList) throws NotFoundException;

    void softDeleteById(UUID id);

}
