package com.example.module.api.file.service;

import com.example.module.api.file.dto.response.ResponseFileCategoryDto;
import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.entity.FileCategory;
import com.example.module.repository.FileCategoryRepository;
import com.example.module.repository.file.FileRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileCategoryRepository fileCategoryRepository;
    @Value("${spring.servlet.multipart.location}")
    private String FILE_PATH;

    public Page<ResponseFileDto> getFileList(HashMap<String, Object> filters, Pageable pageable) {
        return fileRepository.getFileList(filters, pageable);
    }

    public List<ResponseFileCategoryDto> getFileCategoryList() {
        return fileCategoryRepository.findAll()
                .stream()
                .map(ResponseFileCategoryDto::new)
                .collect(Collectors.toList());
    }

    public ResponseEntity<List<Long>> postFilesUpload(String fileCategoryStr, List<MultipartFile> files) {

        // 파일 카테고리 체크
        FileCategory fileCategory  = fileCategoryRepository
                .findByNameAndIsEnabled(fileCategoryStr, true)
                .orElseThrow(() -> new CommonException(ErrorCode.FILE_CATEGORY_NOT_EXISTS));

        // 파일이있는지 체크
        if (files.isEmpty()) {
            throw new CommonException(ErrorCode.FILE_EMPTY);
        }

        validationFile(files);

        return ResponseEntity.ok().body(fileUpload(fileCategory, files));
    }

    private void validationFile(List<MultipartFile> files) {
        // 1. 파일 최대 용량 체크
        long maxSize;
        maxSize = 1024 * 1024 * 1024;
        for (MultipartFile file : files) {
            if (file.getSize() > maxSize) {
                throw new CommonException(ErrorCode.FILE_SIZE_EXCEEDED);
            }

//            // 2-2 파일 형식자가 svg일때는 에러 처리
//            if ("svg".equals(FilenameUtils.getExtension(file.getOriginalFilename()))) {
//                throw new CommonException(ErrorCode.FILE_EXTENSION_INVALID);
//            }
        }
    }

    public List<Long> fileUpload(FileCategory fileCategory, List<MultipartFile> files) {
        String dateDirectory = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        File fileDirectory = new File(FILE_PATH + File.separator + dateDirectory);

        List<Long> resultFileId = new ArrayList<>();

        File finalFileDirectory = fileDirectory;
        files.forEach(file -> {
            String mimeType = file.getContentType();
            String originalFileName = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFileName);
            File newFileName = new File(finalFileDirectory.getPath(), UUID.randomUUID() + "-" + file.getOriginalFilename());

            try {
                if (finalFileDirectory.mkdirs()) {
                    file.transferTo(newFileName);
                    resultFileId.add(
                            fileRepository.save(
                                    com.example.module.entity.File.builder()
                                            .storedName(newFileName.getName())
                                            .originName(originalFileName)
                                            .mime(mimeType)
                                            .path(finalFileDirectory.getPath())
                                            .extension(extension)
                                            .size(file.getSize())
                                            .fileCategory(fileCategory)
                                            .build()
                            ).getId()
                    );
                } else {
                    file.transferTo(newFileName);
                    resultFileId.add(
                            fileRepository.save(
                                    com.example.module.entity.File.builder()
                                            .storedName(newFileName.getName())
                                            .originName(originalFileName)
                                            .mime(mimeType)
                                            .path(finalFileDirectory.getPath())
                                            .extension(extension)
                                            .size(file.getSize())
                                            .fileCategory(fileCategory)
                                            .build()
                            ).getId()
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return resultFileId;
    }
}
