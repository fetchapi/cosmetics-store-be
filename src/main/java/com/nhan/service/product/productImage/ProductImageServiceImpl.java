package com.nhan.service.product.productImage;

import com.nhan.exception.BadRequestException;
import com.nhan.exception.NotFoundException;
import com.nhan.file.CloudinaryService;
import com.nhan.mapper.product.ProductImageMapper;
import com.nhan.model.dto.product.productImage.ProductImageBasicDTO;
import com.nhan.model.dto.response.CloudinaryResponseDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.entity.product.Product;
import com.nhan.model.entity.product.ProductImage;
import com.nhan.repository.product.ProductImageRepository;
import com.nhan.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Value("${maxImageSize:2097152}")
    private Long maxImageSize;

    private static final String PATTERN = "^(.+(gif|jpe?g|tiff?|png|webp|bmp))$";

    private boolean isValidTypeImage(MultipartFile file) {
        Pattern pattern = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(file.getOriginalFilename());
        return matcher.matches();
    }

    @Override
    public ResponseModelDTO uploadImages(UUID productId, List<MultipartFile> fileList) throws NotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Not found product with id: " + productId));

        List<MultipartFile> validImage = new ArrayList<>();
        for(MultipartFile file: fileList) {
            if (!isValidTypeImage(file))
                throw new BadRequestException("Invalid File Format");

            if (file.getSize() > maxImageSize)
                throw new BadRequestException(" This file is over 2MB");

            validImage.add(file);
        }

        List<ProductImage> productImages = new ArrayList<>();

        DateFormat dateFormatter = new SimpleDateFormat("_yyyyMMddHHmmssSSS_");
        String currentDateTime = dateFormatter.format(new Date());

        int count = 1;
        for(MultipartFile file : validImage) {

            String fileName = product.getSlug() + currentDateTime + count;
            CloudinaryResponseDTO cloudinaryResponseDTO = cloudinaryService.saveAs(file, fileName);

            ProductImage productImage = ProductImage
                    .builder()
                    .url(cloudinaryResponseDTO.getUrl())
                    .publicId(cloudinaryResponseDTO.getPublicId())
                    .product(product)
                    .build();

            productImages.add(productImage);
            count++;
        }

        List<ProductImage> productImageList = productImageRepository.saveAll(productImages);

        List<ProductImageBasicDTO> productImageBasicDTOS = new ArrayList<>();
        for(ProductImage productImage : productImageList) {
            productImageBasicDTOS.add(productImageMapper.fromEntityToBasic(productImage));
        }

        return ResponseModelDTO.builder()
                .data(productImageBasicDTOS)
                .isSuccess(true)
                .build();
    }

    @Override
    @Transactional
    public void softDeleteById(UUID id) {
        if (productImageRepository.existsById(id)) {
            productImageRepository.softDeleteById(id);
        } else new NotFoundException("Not found image with id: " + id);
    }
}
