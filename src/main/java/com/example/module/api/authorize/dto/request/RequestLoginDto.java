package com.example.module.api.authorize.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLoginDto {
    @NotBlank(message = "아이디를 입력 해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력 해주세요.")
    private String password;
}
