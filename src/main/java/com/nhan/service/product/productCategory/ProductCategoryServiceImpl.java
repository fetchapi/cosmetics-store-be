package com.nhan.service.product.productCategory;

import com.nhan.exception.BadRequestException;
import com.nhan.exception.NotFoundException;
import com.nhan.mapper.product.ProductCategoryMapper;
import com.nhan.model.dto.product.productCategory.ProductCategoryBasicDTO;
import com.nhan.model.dto.product.productCategory.ProductCategoryCreateDTO;
import com.nhan.model.dto.product.productCategory.ProductCategoryUpdateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.entity.product.ProductCategory;
import com.nhan.repository.product.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public ResponseModelDTO createNewProductCategory(ProductCategoryCreateDTO productCategoryCreateDTO) throws NotFoundException {
        if (productCategoryRepository.existsByCode(productCategoryCreateDTO.getCode())) {
            throw new BadRequestException("Product Category with this code already exists");
        }

        ProductCategory productCategoryParent = null;
        UUID parentId = productCategoryCreateDTO.getParentId();

        if (parentId != null) {
            productCategoryParent = productCategoryRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException("Not found product category with id: " + parentId));
        }

        ProductCategory productCategory = productCategoryMapper.fromCreateToEntity(productCategoryCreateDTO);
        productCategory.setProductCategory(productCategoryParent);
        productCategoryRepository.save(productCategory);

        ProductCategoryBasicDTO productCategoryBasicDTO = productCategoryMapper.fromEntityToBasic(productCategory);
        productCategoryBasicDTO.setParentId(parentId);

        return ResponseModelDTO.builder()
                .data(productCategoryBasicDTO)
                .isSuccess(true)
                .build();

    }

    @Override
    public ResponseModelDTO updateById(UUID id, ProductCategoryUpdateDTO productCategoryUpdateDTO) throws NotFoundException {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product category with id: " + id));

        Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findByCode(productCategoryUpdateDTO.getCode());

        if (productCategoryOptional.isPresent() && !productCategoryOptional.get().getId().equals(id)) {
            throw new BadRequestException("Product Category with this code already exists");
        }

        ProductCategory productCategoryParent = null;
        UUID parentId = productCategoryUpdateDTO.getParentId();

        if (parentId != null) {
            productCategoryParent = productCategoryRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException("Not found product category with id: " + parentId));
        }

        productCategory.setName(productCategoryUpdateDTO.getName());
        productCategory.setCode(productCategoryUpdateDTO.getCode());
        productCategory.setProductCategory(productCategoryParent);

        ProductCategoryBasicDTO detailDTO = productCategoryMapper.fromEntityToBasic(productCategoryRepository.save(productCategory));
        detailDTO.setParentId(parentId);

        return ResponseModelDTO.builder()
                .data(detailDTO)
                .isSuccess(true)
                .build();

    }

    @Override
    public ResponseModelDTO findAll() {
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();

        List<ProductCategoryBasicDTO> productCategoryBasicDTOS = new ArrayList<>();

        for(ProductCategory productCategory : productCategoryList) {
            ProductCategoryBasicDTO productCategoryBasicDTO = productCategoryMapper.fromEntityToBasic(productCategory);
            if (productCategory.getProductCategory() != null) {
                productCategoryBasicDTO.setParentId(productCategory.getProductCategory().getId());
            }
            productCategoryBasicDTOS.add(productCategoryBasicDTO);
        }

        return ResponseModelDTO.builder()
                .data(productCategoryBasicDTOS)
                .isSuccess(true)
                .build();
    }

    @Override
    public ResponseModelDTO findById(UUID id) throws NotFoundException {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product category with id: " + id));

        ProductCategoryBasicDTO productCategoryBasicDTO = productCategoryMapper.fromEntityToBasic(productCategory);
        if (productCategory.getProductCategory() != null) {
            productCategoryBasicDTO.setParentId(productCategory.getProductCategory().getId());
        }

        return ResponseModelDTO.builder()
                .data(productCategoryBasicDTO)
                .isSuccess(true)
                .build();
    }

    @Override
    @Transactional
    public void softDeleteById(UUID id) throws NotFoundException {
        if (productCategoryRepository.existsById(id)) {
            List<ProductCategory> productCategoryList = productCategoryRepository.findAllByParentId(id);
            for(ProductCategory productCategory : productCategoryList) {
                productCategoryRepository.softDeleteById(productCategory.getId());
            }
            productCategoryRepository.softDeleteById(id);
        }

        else throw new NotFoundException("Not found product category with id: " + id);
    }
}
