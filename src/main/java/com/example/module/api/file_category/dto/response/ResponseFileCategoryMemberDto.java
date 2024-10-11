package com.example.module.api.file_category.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseFileCategoryMemberDto {
    private Long id; // 회원 pk
    private String userId; // 아이디
    private String name; // 회원 이름
    private String email; // 회원 이메일
    private String sex; // 회원 성별
    private boolean permission_status; // 권한 여부

}
