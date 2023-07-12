package com.nhan.service.product;

import com.nhan.exception.BadRequestException;
import com.nhan.exception.NotFoundException;
import com.nhan.file.CloudinaryService;
import com.nhan.mapper.product.ProductBrandMapper;
import com.nhan.mapper.product.ProductCategoryMapper;
import com.nhan.mapper.product.ProductMapper;
import com.nhan.model.dto.product.ProductBasicDTO;
import com.nhan.model.dto.product.ProductCreateDTO;
import com.nhan.model.dto.product.ProductDetailDTO;
import com.nhan.model.dto.product.ProductUpdateDTO;
import com.nhan.model.dto.response.CloudinaryResponseDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.response.ResponsePageDTO;
import com.nhan.model.entity.product.Product;
import com.nhan.model.entity.product.ProductBrand;
import com.nhan.model.entity.product.ProductCategory;
import com.nhan.repository.product.ProductBrandRepository;
import com.nhan.repository.product.ProductCategoryRepository;
import com.nhan.repository.product.ProductRepository;
import com.nhan.utils.SlugUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductBrandRepository productBrandRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductBrandMapper productBrandMapper;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private SlugUtil slugUtil;


    @Value("${maxImageSize:2097152}")
    private Long maxImageSize;

    private static final String PATTERN = "^(.+(gif|jpe?g|tiff?|png|webp|bmp))$";

    private boolean isValidTypeImage(MultipartFile file) {
        Pattern pattern = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(file.getOriginalFilename());
        return matcher.matches();
    }

    @Transactional
    private void saveImage(MultipartFile file, Product product) {
        if (!isValidTypeImage(file))
            throw new BadRequestException("Invalid File Format");

        if (file.getSize() > maxImageSize)
            throw new BadRequestException(" This file is over 2MB");

        DateFormat dateFormatter = new SimpleDateFormat("_yyyyMMddHHmmssSSS");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = product.getSlug() + currentDateTime;
        CloudinaryResponseDTO cloudinaryResponseDTO = cloudinaryService.saveAs(file, fileName);
        product.setImageUrl(cloudinaryResponseDTO.getUrl());
    }


    @Override
    @Transactional
    public ResponseModelDTO createNewProduct(ProductCreateDTO productCreateDTO, MultipartFile image) throws NotFoundException {

        UUID productCategoryId = productCreateDTO.getProductCategoryId();
        UUID productBrandId = productCreateDTO.getProductBrandId();

        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new NotFoundException("Not found product category with id: " + productCategoryId));

        ProductBrand productBrand = productBrandRepository.findById(productBrandId)
                .orElseThrow(() -> new NotFoundException("Not found product brand with id: " + productBrandId));

        String slug = slugUtil.toSlug(productCreateDTO.getName());

        if (productRepository.existsBySlug(slug)) {
            throw new BadRequestException("Product with this name already exists");
        }

        Product product = productMapper.fromCreateToEntity(productCreateDTO);
        product.setSlug(slug);
        product.setProductCategory(productCategory);
        product.setProductBrand(productBrand);

        saveImage(image, product);

        productRepository.save(product);

        return ResponseModelDTO.builder()
                .data(productMapper.fromEntityToBasic(product))
                .isSuccess(true)
                .build();
    }

    @Override
    public ResponsePageDTO findAll(String keyword, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(keyword, pageable);

        List<ProductBasicDTO> productBasicDTOS = new ArrayList<>();

        for(Product product : productPage.getContent()) {
            ProductBasicDTO productBasicDTO = productMapper.fromEntityToBasic(product);
            productBasicDTOS.add(productBasicDTO);
        }

        return ResponsePageDTO.builder()
                .data(productBasicDTOS)
                .limit(productPage.getSize())
                .currentPage(productPage.getNumber())
                .totalItems(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public ResponsePageDTO filterByCategoryList(List<UUID> uuids, Pageable pageable) {
        Page<Product> productPage = productRepository.filterByCategoryList(uuids, pageable);

        List<ProductBasicDTO> productBasicDTOS = new ArrayList<>();

        for(Product product : productPage.getContent()) {
            ProductBasicDTO productBasicDTO = productMapper.fromEntityToBasic(product);
            productBasicDTOS.add(productBasicDTO);
        }

        return ResponsePageDTO.builder()
                .data(productBasicDTOS)
                .limit(productPage.getSize())
                .currentPage(productPage.getNumber())
                .totalItems(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public ResponseModelDTO findById(UUID id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not product brand with id: " + id));

        ProductDetailDTO productDetailDTO = productMapper.fromEntityToDetail(product);
        productDetailDTO.setProductCategory(productCategoryMapper.fromEntityToBasic(product.getProductCategory()));
        productDetailDTO.setProductBrand(productBrandMapper.fromEntityToDetail(product.getProductBrand()));

        return ResponseModelDTO.builder()
                .data(productDetailDTO)
                .isSuccess(true)
                .build();
    }

    @Override
    public ResponseModelDTO updateById(UUID id, ProductUpdateDTO productUpdateDTO) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not product brand with id: " + id));

        UUID productCategoryId = productUpdateDTO.getProductCategoryId();
        UUID productBrandId = productUpdateDTO.getProductBrandId();

        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new NotFoundException("Not found product category with id: " + productCategoryId));

        ProductBrand productBrand = productBrandRepository.findById(productBrandId)
                .orElseThrow(() -> new NotFoundException("Not found product brand with id: " + productBrandId));


        String slug = slugUtil.toSlug(productUpdateDTO.getName());

        Optional<Product> productOptional = productRepository.findBySlug(slug);

        if (productOptional.isPresent() && !productOptional.get().getId().equals(id)) {
            throw new BadRequestException("Product with this code already exists");
        }

        product.setName(productUpdateDTO.getName());
        product.setSlug(slug);
        product.setDescription(productUpdateDTO.getDescription());
        product.setPrice(productUpdateDTO.getPrice());
        product.setDiscount(productUpdateDTO.getDiscount());
        product.setQuantity(productUpdateDTO.getQuantity());
        product.setProductCategory(productCategory);
        product.setProductBrand(productBrand);

        productRepository.save(product);

        return ResponseModelDTO.builder()
                .data(productMapper.fromEntityToBasic(product))
                .isSuccess(true)
                .build();
    }

    @Override
    public void softDeleteById(UUID id) throws NotFoundException {
        if(productRepository.existsById(id)) {
            productRepository.softDeleteById(id);
        } else throw new NotFoundException("Not found product with id: " + id);
    }
}
