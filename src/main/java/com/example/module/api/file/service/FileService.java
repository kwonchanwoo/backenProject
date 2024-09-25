package com.example.module.api.file.service;

import com.example.module.api.file.dto.response.ResponseFileCategoryDto;
import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.repository.FileCategoryRepository;
import com.example.module.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileCategoryRepository fileCategoryRepository;

    public Page<ResponseFileDto> getFileList(HashMap<String, Object> filters, Pageable pageable) {
        return fileRepository.getFileList(filters, pageable);
    }

    public List<ResponseFileCategoryDto> getFileCategoryList() {
        return fileCategoryRepository.findAll()
                .stream()
                .map(ResponseFileCategoryDto::new)
                .collect(Collectors.toList());
    }
}
