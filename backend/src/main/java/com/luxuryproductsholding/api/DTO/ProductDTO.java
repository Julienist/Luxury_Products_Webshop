package com.luxuryproductsholding.api.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDTO {

    private String name;
    private String description;
    private double price;
    private String imageUrl;
//    public int stockQuantity; Doesn't have a stock quantity in the Product model
    @JsonAlias("categoryId")
    private Long categoryId; // Using JsonAlias to map to categoryId in JSON requests

}
