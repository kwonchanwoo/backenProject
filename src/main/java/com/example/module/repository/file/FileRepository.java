package com.example.module.repository.file;

import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface FileRepository extends JpaRepository<File, Long>, FileCustomRepository {
    Page<ResponseFileDto> getFileList(Map<String, Object> filters, Pageable pageable);
}
