package com.example.module.api.file.dto.response;

import com.example.module.entity.File;
import com.example.module.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFileDto {
    private String fileName;
    private String description;
    private String memberName;
    private long size;

    public ResponseFileDto(File file) {
        this.fileName = file.getName();
        this.description = file.getDescription();
        this.size = file.getSize();
        this.memberName = file.getMember().getName();
    }

}
