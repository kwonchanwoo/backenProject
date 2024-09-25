package com.example.module.api.file.dto.response;

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
    private boolean enabled;

    public ResponseFileCategoryDto(FileCategory fileCategory) {
        this.id = fileCategory.getId();
        this.name = fileCategory.getName();
        this.enabled = fileCategory.isEnabled();
    }
}
