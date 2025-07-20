package com.cumulus.backend.club.dto;

import com.cumulus.backend.club.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryDto {
    private final int id;
    private final String name;
    private final String label;

    public static CategoryDto from(Category category){
        return new CategoryDto(category.getId(), category.name(), category.getLabel());
    }
}
