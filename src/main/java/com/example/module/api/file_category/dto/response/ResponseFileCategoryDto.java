package com.example.module.api.file_category.dto.response;

import com.example.module.entity.FileCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseFileCategoryDto {
    private Long id;
    private String name;

    public ResponseFileCategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
