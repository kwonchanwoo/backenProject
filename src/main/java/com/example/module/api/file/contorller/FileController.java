package com.example.module.api.file.contorller;

import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.api.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("files")
public class FileController {
    private final FileService fileService;

    @GetMapping
    public Page<ResponseFileDto> getMemberList(@RequestParam(required = false) HashMap<String,Object> filters, Pageable pageable){
        return fileService.getFileList(filters, pageable);
    }

}
