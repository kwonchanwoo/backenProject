package com.example.module.api.admin.service;

import com.example.module.api.admin.dto.RequestFileCategoryRole;
import com.example.module.entity.FileCategory;
import com.example.module.entity.FileCategoryRole;
import com.example.module.entity.Member;
import com.example.module.repository.FileCategoryRepository;
import com.example.module.repository.FileCategoryRoleRepository;
import com.example.module.repository.member.MemberRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.example.module.util.dto.FileCategoryRolePK;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final FileCategoryRepository fileCategoryRepository;
    private final FileCategoryRoleRepository fileCategoryRoleRepository;
    private final MemberRepository memberRepository;

    public void postFileCategory(String fileCategory) {
        // 파일 카테고리 중복 체크
        if(fileCategoryRepository.findByName(fileCategory).isPresent()){
            throw new CommonException(ErrorCode.FILE_CATEGORY_DUPLICATED);
        }
        fileCategoryRepository.save(
                FileCategory.builder()
                        .name(fileCategory)
                        .build()
        );
    }

    @Transactional
    public void postFileCategoryRole(RequestFileCategoryRole requestFileCategoryRole) {
        Member member = memberRepository
                .findById(requestFileCategoryRole.getMember_id())
                .orElseThrow(() -> new CommonException(ErrorCode.MEMBER_NOT_FOUND));

        List<FileCategory> fileCategory = fileCategoryRepository
                .findByIdIn(requestFileCategoryRole.getFile_category_id());

        if(fileCategory.isEmpty()) throw new CommonException(ErrorCode.FILE_CATEGORY_NOT_EXISTS);

        List<FileCategoryRole> fileCategoryRoles = new ArrayList<>();

        for (FileCategory category : fileCategory) {
            fileCategoryRoles.add(FileCategoryRole.builder().fileCategoryRolePK(new FileCategoryRolePK(member,category)).build());
        }

        fileCategoryRoleRepository.deleteAll(fileCategoryRoleRepository.findByFileCategoryRolePK_Member(member));

        fileCategoryRoleRepository.saveAll(fileCategoryRoles);
    }
}
