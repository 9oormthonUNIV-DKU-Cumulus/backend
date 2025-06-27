package com.cumulus.backend.activity.dto;

import com.cumulus.backend.common.Category;
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
