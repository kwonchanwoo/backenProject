package com.example.module.api.admin.service;

import com.example.module.api.admin.dto.request.RequestFileCategoryRoleDto;
import com.example.module.api.admin.dto.request.RequestPatchMemberDto;
import com.example.module.api.member.dto.request.RequestMemberDto;
import com.example.module.entity.FileCategory;
import com.example.module.entity.FileCategoryRole;
import com.example.module.entity.Member;
import com.example.module.repository.FileCategoryRoleRepository;
import com.example.module.repository.file_category.FileCategoryRepository;
import com.example.module.repository.member.MemberRepository;
import com.example.module.util.BaseEntity;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.example.module.util.dto.FileCategoryRolePK;
import com.example.module.util.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final FileCategoryRepository fileCategoryRepository;
    private final FileCategoryRoleRepository fileCategoryRoleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void postFileCategory(String fileCategoryName) {
        // 파일 카테고리 중복 체크
        if (fileCategoryRepository.findByNameAndDeletedFalse(fileCategoryName).isPresent()) {
            throw new CommonException(ErrorCode.FILE_CATEGORY_DUPLICATED);
        }
        fileCategoryRepository.save(
                FileCategory.builder()
                        .name(fileCategoryName)
                        .build()
        );
    }

    @Transactional
    public void postFileCategoryRole(RequestFileCategoryRoleDto requestFileCategoryRoleDto) {

        FileCategory fileCategory = fileCategoryRepository
                .findById(requestFileCategoryRoleDto.getFile_category_id()).
                orElseThrow(() -> new CommonException(ErrorCode.FILE_CATEGORY_NOT_EXISTS));

        // 파일 카테고리 권한 철회 리스트
        List<Member> revokeMemberList = memberRepository.findByIdInAndDeletedFalse(requestFileCategoryRoleDto.getRevokeMemberIds());

        // 파일 카테고리 권한 부여 리스트
        List<Member> assignMemberList = memberRepository.findByIdInAndDeletedFalse(requestFileCategoryRoleDto.getAssignMemberIds());

        List<FileCategoryRole> revokeFileCategoryRoles = new ArrayList<>();
        List<FileCategoryRole> assignFileCategoryRoles = new ArrayList<>();


        for (Member member : revokeMemberList) {
            revokeFileCategoryRoles.add(FileCategoryRole.builder().fileCategoryRolePK(new FileCategoryRolePK(member, fileCategory)).deleted(true).build());
        }

        if (!revokeFileCategoryRoles.isEmpty()) {
            fileCategoryRoleRepository.saveAll(revokeFileCategoryRoles);
        }


        for (Member member : assignMemberList) {
            assignFileCategoryRoles.add(FileCategoryRole.builder().fileCategoryRolePK(new FileCategoryRolePK(member, fileCategory)).deleted(false).build());
        }

        if (!assignFileCategoryRoles.isEmpty()) {
            fileCategoryRoleRepository.saveAll(assignFileCategoryRoles);
        }
    }

    @Transactional
    public void deleteFileCategory(Long fileCategoryId) {
        FileCategory fileCategory = fileCategoryRepository
                .findByIdAndDeletedFalse(fileCategoryId)
                .orElseThrow(() -> new CommonException(ErrorCode.FILE_CATEGORY_NOT_EXISTS));

        if (fileCategory.getFiles().stream().anyMatch(file -> !file.isDeleted())) { // 삭제되지 않은 파일만 체크
            throw new CommonException(ErrorCode.FILE_NOT_EMPTY);
        }

        fileCategory.setDeleted(true);
        fileCategoryRepository.save(fileCategory);
    }

    @Transactional
    public void patchMember(RequestPatchMemberDto requestPatchMemberDto) {

        if (!SecurityContextHelper.isAdmin()) {
            throw new CommonException(ErrorCode.ACCESS_DENIED);
        }

        Member member = memberRepository
                .findByIdAndDeletedFalse(requestPatchMemberDto.getMemberId())
                .orElseThrow(() -> new CommonException(ErrorCode.MEMBER_NOT_FOUND));

        List<String> roles = new ArrayList<>();

        roles.add(requestPatchMemberDto.getRole());

        member.setRoles(roles);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {

        if (!SecurityContextHelper.isAdmin()) {
            throw new CommonException(ErrorCode.ACCESS_DENIED);
        }

        Member member = memberRepository
                .findByIdAndDeletedFalse(memberId)
                .orElseThrow(() -> new CommonException(ErrorCode.MEMBER_NOT_FOUND));

//        if (member.hasRole("ADMIN")) {
//            throw new CommonException(ErrorCode.ADMIN_CANNOT_DELETE_ADMIN);
//        }
        
        member.setDeleted(true);
        memberRepository.save(member);
    }
}
