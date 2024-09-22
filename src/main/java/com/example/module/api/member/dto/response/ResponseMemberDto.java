package com.example.module.api.member.dto.response;

import com.example.module.entity.Member;
import com.example.module.util._Enum.Gender;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMemberDto {
    private String id;
    private String email;
    private String name;
    private Gender sex;
    private int age;
    private String phoneNumber;

    public ResponseMemberDto(Member member) {
        this.id = member.getUserId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.sex = member.getSex();
        this.age = member.getAge();
        this.phoneNumber = member.getPhoneNumber();
    }
}
