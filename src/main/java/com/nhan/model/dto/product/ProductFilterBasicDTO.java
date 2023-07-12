package com.nhan.model.dto.product;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductFilterBasicDTO {

    private UUID id;

    private String name;

    private String slug;

    private Long price;

    private int discount;

    private int quantity;

    private String imageUrl;

}
