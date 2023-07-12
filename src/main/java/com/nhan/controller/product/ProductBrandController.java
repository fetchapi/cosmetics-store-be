package com.nhan.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.product.productBrand.ProductBrandCreateDTO;
import com.nhan.model.dto.product.productBrand.ProductBrandUpdateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.service.product.productBrand.ProductBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/product-brands")
public class ProductBrandController {

    @Autowired
    private ProductBrandService productBrandService;

    @GetMapping
    @Operation(summary = "Find all brands")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all brands successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),

    })
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(productBrandService.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a new brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a new brand successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT_BRAND', 'PRODUCT_BRAND_CREATE')")
    public ResponseEntity<ResponseModelDTO> create(@Valid @RequestBody ProductBrandCreateDTO productBrandCreateDTO) throws NotFoundException {

        return ResponseEntity.ok().body(productBrandService.createNewProductBrand(productBrandCreateDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update a brand successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT_BRAND', 'PRODUCT_BRAND_EDIT')")
    public ResponseEntity<ResponseModelDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductBrandUpdateDTO productBrandUpdateDTO) throws NotFoundException {

        return ResponseEntity.ok().body(productBrandService.updateById(id, productBrandUpdateDTO));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete a brand successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT_BRAND', 'PRODUCT_BRAND_DELETE')")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws JsonProcessingException, NotFoundException {
        productBrandService.softDeleteById(id);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"message\":\"Delete brand successfully\"}");

        return ResponseEntity.ok().body(json);
    }

}
