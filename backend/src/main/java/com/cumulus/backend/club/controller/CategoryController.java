package com.cumulus.backend.club.controller;

import com.cumulus.backend.club.dto.CategoryDto;
import com.cumulus.backend.common.ApiResponse;
import com.cumulus.backend.club.domain.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Tag(name = "동아리 카테고리")
@RestController
public class CategoryController {

    @Operation(summary = "홈화면 - 동아리 카테고리 목록조회",
            description = "동아리 전체 카테고리 조회 - 스포츠, 외국/언어, 사진/영상, " +
            "봉사활동, 자기계발, 독서/글, 문화/공연, 음악/악기, 여행, 업종/직무")
    @GetMapping("/api/categories")
    public ResponseEntity<ApiResponse<?>> getCategories(){
        List<CategoryDto> categories =
                Arrays.stream(Category.values())
                        .map(CategoryDto::from)
                        .toList();

        return ResponseEntity.ok(ApiResponse.success(categories));
    }
}
