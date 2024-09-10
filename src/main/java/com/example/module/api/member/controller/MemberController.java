package com.example.module.api.member.controller;

import com.example.module.api.member.service.MemberService;
import com.example.module.api.member.dto.request.RequestMemberDto;
import com.example.module.api.member.dto.response.ResponseMemberDto;
import com.example.module.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{member}")
    public ResponseMemberDto getMember(@PathVariable Member member){
        return new ResponseMemberDto(member);
    }

    // 가입
    @PostMapping("/join")
    public void join(@Valid @RequestBody RequestMemberDto memberCreateDto) {
        memberService.join(memberCreateDto);
    }

    // 회원 탈퇴
    @DeleteMapping("/{member}")
    public void deleteMember(
            @PathVariable Member member
    ) {
        memberService.deleteMember(member);
    }

}
