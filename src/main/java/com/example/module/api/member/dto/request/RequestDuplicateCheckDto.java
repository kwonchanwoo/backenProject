package com.example.module.api.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDuplicateCheckDto {
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
