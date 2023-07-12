package com.nhan.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.product.ProductCreateDTO;
import com.nhan.model.dto.product.ProductUpdateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.response.ResponsePageDTO;
import com.nhan.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "Find all products with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all products with pagination successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponsePageDTO.class))),

    })
    public ResponseEntity<?> findAllWithPagination(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page, //page number
            @RequestParam(name = "limit", defaultValue = "20") int limit, //page size
            @RequestParam(name = "orderBy", defaultValue = "name") String orderBy, //database field
            @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
    ) {
        Sort sort = Sort.by(sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return ResponseEntity.ok().body(productService.findAll(keyword, pageable));
    }

    @GetMapping("/filter/category")
    @Operation(summary = "Filter products by category list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filter products by category list successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponsePageDTO.class))),

    })
    public ResponseEntity<?> filterByCategoryList(
            @RequestParam(name = "categoryList") List<UUID> uuids,
            @RequestParam(name = "page", defaultValue = "0") int page, //page number
            @RequestParam(name = "limit", defaultValue = "20") int limit, //page size
            @RequestParam(name = "orderBy", defaultValue = "name") String orderBy, //database field
            @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
    ) {
        Sort sort = Sort.by(sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return ResponseEntity.ok().body(productService.filterByCategoryList(uuids, pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search products successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponsePageDTO.class))),

    })
    public ResponseEntity<?> searchProduct(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page, //page number
            @RequestParam(name = "limit", defaultValue = "20") int limit, //page size
            @RequestParam(name = "orderBy", defaultValue = "name") String orderBy, //database field
            @RequestParam(name = "sortBy", defaultValue = "asc") String sortBy
    ) {
        Sort sort = Sort.by(sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);
        Pageable pageable = PageRequest.of(page, limit, sort);

        return ResponseEntity.ok().body(productService.searchProduct(keyword, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find a product successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),

    })
    public ResponseEntity<?> findById(@PathVariable UUID id) throws NotFoundException {

        return ResponseEntity.ok().body(productService.findById(id));
    }

    @PostMapping()
    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a new product successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT', 'PRODUCT_CREATE')")
    public ResponseEntity<?> create(
            @Valid @RequestPart ProductCreateDTO productCreateDTO,
            @RequestPart MultipartFile image) throws IOException, NotFoundException {

        return ResponseEntity.ok().body(productService.createNewProduct(productCreateDTO, image));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update a product successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT', 'PRODUCT_EDIT')")
    public ResponseEntity<?> updateById(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateDTO productUpdateDTO) throws NotFoundException {

        return ResponseEntity.ok().body(productService.updateById(id, productUpdateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete a product successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseModelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")

    })
    @PreAuthorize("hasAuthority('SUPER_ADMIN') || hasPermission('PRODUCT', 'PRODUCT_DELETE')")
    public ResponseEntity<?> delete(@PathVariable UUID id) throws JsonProcessingException, NotFoundException {
        productService.softDeleteById(id);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"message\":\"Delete product successfully\"}");

        return ResponseEntity.ok().body(json);
    }

}
