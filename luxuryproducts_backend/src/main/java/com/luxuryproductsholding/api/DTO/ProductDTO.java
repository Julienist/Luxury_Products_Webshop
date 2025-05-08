package com.luxuryproductsholding.api.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProductDTO {

    public String name;
    public String description;
    public double price;
    public String imageUrl;
    public int stockQuantity;

    public ProductDTO(String name, String description, double price, String imageUrl, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
    }

}
