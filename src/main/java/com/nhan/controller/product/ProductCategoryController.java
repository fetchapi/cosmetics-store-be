package com.nhan.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.product.productCategory.ProductCategoryCreateDTO;
import com.nhan.model.dto.product.productCategory.ProductCategoryUpdateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.service.product.productCategory.ProductCategoryService;
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
@RequestMapping("api/v1/product-categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    @Operation(summary = "Find all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all categories successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),

    })
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(productCategoryService.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a new category successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT_CATEGORY', 'PRODUCT_CATEGORY_CREATE')")
    public ResponseEntity<ResponseModelDTO> create(@Valid @RequestBody ProductCategoryCreateDTO productCategoryCreateDTO) throws NotFoundException {

        return ResponseEntity.ok().body(productCategoryService.createNewProductCategory(productCategoryCreateDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update a category successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT_CATEGORY', 'PRODUCT_CATEGORY_EDIT')")
    public ResponseEntity<ResponseModelDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductCategoryUpdateDTO productCategoryUpdateDTO) throws NotFoundException {

        return ResponseEntity.ok().body(productCategoryService.updateById(id, productCategoryUpdateDTO));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete a category successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT_CATEGORY', 'PRODUCT_CATEGORY_DELETE')")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws JsonProcessingException, NotFoundException {
        productCategoryService.softDeleteById(id);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"message\":\"Delete category successfully\"}");

        return ResponseEntity.ok().body(json);
    }

}
