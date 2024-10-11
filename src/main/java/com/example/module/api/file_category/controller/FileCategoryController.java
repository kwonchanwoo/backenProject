package com.example.module.api.file_category.controller;

import com.example.module.api.file_category.dto.response.ResponseFileCategoryDto;
import com.example.module.api.file_category.service.FileCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("fileCategories")
@RestController
@RequiredArgsConstructor
public class FileCategoryController {
    private final FileCategoryService fileCategoryService;

    @GetMapping
    public List<ResponseFileCategoryDto> getFileCategoryList(){
        return fileCategoryService.getFileCategoryList();
    }
}
