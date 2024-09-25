package com.example.module.api.admin.service;

import com.example.module.entity.FileCategory;
import com.example.module.repository.FileCategoryRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final FileCategoryRepository fileCategoryRepository;

    public void addFileCatregory(String fileCategory) {
        // 파일 카테고리 중복 체크
        if(fileCategoryRepository.findByName(fileCategory).isPresent()){
            throw new CommonException(ErrorCode.FILE_CATEGORY_DUPLICATED);
        }
        fileCategoryRepository.save(
                FileCategory.builder()
                        .name(fileCategory)
                        .build()
        );
    }
}
