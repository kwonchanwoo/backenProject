package com.example.module.repository.file;

import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.api.member.dto.response.ResponseMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface FileCustomRepository {
        Page<ResponseFileDto> getFileList(Map<String, Object> filters, Pageable pageable);

}
