package com.example.module.api.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFileDto {
    private Long id;
    private String fileName;
    private String fileCategory;
    private String description;
    private String memberName;
    private String userId;
    private long size;

}
