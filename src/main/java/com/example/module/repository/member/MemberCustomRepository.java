package com.example.module.repository.member;

import com.example.module.api.member.dto.response.ResponseMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface MemberCustomRepository {
    Page<ResponseMemberDto> getMemberList(Map<String, Object> filters, Pageable pageable);
}
