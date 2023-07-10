package com.nhan.service.product.productBrand;

import com.nhan.exception.BadRequestException;
import com.nhan.exception.NotFoundException;
import com.nhan.mapper.product.ProductBrandMapper;
import com.nhan.model.dto.product.productBrand.ProductBrandCreateDTO;
import com.nhan.model.dto.product.productBrand.ProductBrandDetailDTO;
import com.nhan.model.dto.product.productBrand.ProductBrandUpdateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.entity.product.ProductBrand;
import com.nhan.repository.product.ProductBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductBrandServiceImpl implements ProductBrandService {

    @Autowired
    private ProductBrandRepository productBrandRepository;

    @Autowired
    private ProductBrandMapper productBrandMapper;


    @Override
    public ResponseModelDTO createNewProductBrand(ProductBrandCreateDTO productBrandCreateDTO) throws NotFoundException {
        if (productBrandRepository.existsByCode(productBrandCreateDTO.getCode())) {
            throw new BadRequestException("Product Brand with this code already exists");
        }

        ProductBrand productBrand = productBrandMapper.fromCreateToEntity(productBrandCreateDTO);

        productBrandRepository.save(productBrand);

        return ResponseModelDTO.builder()
                .data(productBrandMapper.fromEntityToDetail(productBrand))
                .isSuccess(true)
                .build();
    }

    @Override
    public ResponseModelDTO updateById(UUID id, ProductBrandUpdateDTO productBrandUpdateDTO) throws NotFoundException {
        ProductBrand productBrand = productBrandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product brand with id: " + id));

        Optional<ProductBrand> productBrandOptional = productBrandRepository.findByCode(productBrandUpdateDTO.getCode());
        if (productBrandOptional.isPresent() && !productBrandOptional.get().getId().equals(id)) {
            throw new BadRequestException("Product Brand with this code already exists");
        }

        productBrand.setName(productBrandUpdateDTO.getName());
        productBrand.setCode(productBrandUpdateDTO.getCode());

        productBrandRepository.save(productBrand);

        return ResponseModelDTO.builder()
                .data(productBrandMapper.fromEntityToDetail(productBrand))
                .isSuccess(true)
                .build();
    }

    @Override
    public ResponseModelDTO findAll() {
        List<ProductBrand> productBrands = productBrandRepository.findAll();

        List<ProductBrandDetailDTO> productBrandDetailDTOS = new ArrayList<>();

        for(ProductBrand productBrand : productBrands) {
            productBrandDetailDTOS.add(productBrandMapper.fromEntityToDetail(productBrand));
        }

        return ResponseModelDTO.builder()
                .data(productBrandDetailDTOS)
                .isSuccess(true)
                .build();
    }

    @Override
    public ResponseModelDTO findById(UUID id) throws NotFoundException {
        ProductBrand productBrand = productBrandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found product brand with id: " + id));

        return ResponseModelDTO.builder()
                .data(productBrandMapper.fromEntityToDetail(productBrand))
                .isSuccess(true)
                .build();
    }

    @Override
    @Transactional
    public void softDeleteById(UUID id) throws NotFoundException {
        if(productBrandRepository.existsById(id)) {
            productBrandRepository.softDeleteById(id);
        } else throw new NotFoundException("Not found product category with id: " + id);
    }
}
