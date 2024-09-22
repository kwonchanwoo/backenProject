package com.example.module.api.file.dto.response;

import com.example.module.entity.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFileDto {
    private String name;
    private String description;
    private long size;

    public ResponseFileDto(File file) {
        this.name = file.getName();
        this.description = file.getDescription();
        this.size = file.getSize();

        // member key값 어디서 넣누
    }

}
