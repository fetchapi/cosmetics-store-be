package com.nhan.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.service.product.productImage.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/product-images")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping(value = "/product/{productId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "Upload images for a product", summary = "Upload images for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload images for a product successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT', 'PRODUCT_CREATE')")
    public ResponseEntity<ResponseModelDTO> uploadImages(
            @PathVariable UUID productId,
            @RequestPart List<MultipartFile> files) throws NotFoundException {

        return ResponseEntity.ok().body(productImageService.uploadImages(productId, files));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(description = "Delete a product image by id", summary = "Delete a product image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete a product image by id successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT', 'PRODUCT_DELETE')")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) throws NotFoundException, JsonProcessingException {

        productImageService.softDeleteById(id);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"message\":\"Delete product image successfully\"}");

        return ResponseEntity.ok().body(json);
    }

}
