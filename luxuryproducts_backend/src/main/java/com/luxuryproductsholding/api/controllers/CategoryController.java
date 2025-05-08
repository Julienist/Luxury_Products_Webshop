package com.luxuryproductsholding.api.controllers;

import com.luxuryproductsholding.api.DAO.CategoryDAO;
import com.luxuryproductsholding.api.models.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryDAO categoryDAO;

    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(this.categoryDAO.getAllCategories());
    }

}
