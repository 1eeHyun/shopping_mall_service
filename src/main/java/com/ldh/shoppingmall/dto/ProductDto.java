package com.ldh.shoppingmall.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductDto {

    private String productName;
    private String description;
    private String imageUrl;
    private double price;
}
