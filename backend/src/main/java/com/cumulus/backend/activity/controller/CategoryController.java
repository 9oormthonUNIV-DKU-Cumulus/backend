package com.cumulus.backend.activity.controller;

import com.cumulus.backend.activity.dto.CategoryDto;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.common.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class CategoryController {

    @GetMapping("/api/categories")
    public ResponseEntity<ApiResponse<?>> getCategories(){
        List<CategoryDto> categories =
                Arrays.stream(Category.values())
                        .map(CategoryDto::from)
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(categories));
    }
}
