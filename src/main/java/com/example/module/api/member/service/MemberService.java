package com.example.module.api.member.service;

import com.example.module.api.member.dto.request.RequestMemberDto;
import com.example.module.entity.Member;
import com.example.module.repository.MemberRepository;
import com.example.module.repository.TokenRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.example.module.util.security.JwtTokenProvider;
import com.example.module.util.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    @Transactional
    public void join(RequestMemberDto memberCreateDto) {
        memberRepository.findByEmail(memberCreateDto.getEmail())
                .ifPresent((member -> {
                    throw new CommonException(ErrorCode.MEMBER_DUPLICATED);
                }));
        memberRepository.save(
                Member.builder()
                        .name(memberCreateDto.getName())
                        .email(memberCreateDto.getEmail())
                        .password(passwordEncoder.encode(memberCreateDto.getPassword()))
                        .sex(memberCreateDto.getSex())
                        .age(memberCreateDto.getAge())
                        .phoneNumber(memberCreateDto.getPhoneNumber())
                        .roles(List.of("USER"))
                        .build());

    }

    @Transactional
    public void deleteMember(Member member) {
        // 관리자/일반유저 구분
        SecurityContextHelper.isAuthorizedForMember(member);

        tokenRepository.deleteByTokenKey(member.getEmail());
    }

//    private String resolveToken(String accessToken) {
//        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer")) {
//            return accessToken.substring(7);
//        }
//        return null;
//    }
}
