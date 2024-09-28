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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public ResponseEntity<List<Long>> fileUpload(String fileCategoryStr, List<MultipartFile> files) {

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

        files.forEach(file -> {
            String mimeType = file.getContentType();

            String originalFileName;

            if (Objects.requireNonNull(file.getOriginalFilename(),"fileName not null or empty").lastIndexOf(".") != -1) {
                originalFileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
            } else {
                // 확장자가 없는 경우 파일 이름 전체를 그대로 사용
                originalFileName = file.getOriginalFilename();
            }


            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            File newFileName = new File(fileDirectory.getPath(), UUID.randomUUID() + "-" + file.getOriginalFilename());

            try {
                if (fileDirectory.mkdirs()) {
                    file.transferTo(newFileName);
                    resultFileId.add(
                            fileRepository.save(
                                    com.example.module.entity.File.builder()
                                            .storedName(newFileName.getName())
                                            .originName(originalFileName)
                                            .mime(mimeType)
                                            .path(fileDirectory.getPath())
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
                                            .path(fileDirectory.getPath())
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

    public ResponseEntity<Resource> fileDownload(com.example.module.entity.File file) {
        try {
            // 파일 경로 지정
            Path filePath = Paths.get(file.getPath()).resolve(file.getStoredName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginName() + "." + file.getExtension() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
