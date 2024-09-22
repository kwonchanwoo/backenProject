package com.example.module.api.file.service;

import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public Page<ResponseFileDto> getFileList(HashMap<String, Object> filters, Pageable pageable) {
        return fileRepository.getFileList(filters, pageable);
    }

}
