package com.example.module.api.admin.controller;

import com.example.module.api.admin.dto.request.RequestFileCategoryRoleDto;
import com.example.module.api.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    /**
     * 파일 카테고리 추가
     *
     * @param fileCategoryName
     */
    @PostMapping("/fileCategory/{fileCategoryName}")
    @ResponseStatus(HttpStatus.OK)
    public void postFileCategory(@PathVariable String fileCategoryName) {
        adminService.postFileCategory(fileCategoryName);
    }

    /**
     * 파일 카테고리 권한 제어
     *
     * @param requestFileCategoryRoleDto
     */
    @PostMapping("/fileCategory/role")
    @ResponseStatus(HttpStatus.OK)
    public void postFileCategoryRole(@RequestBody RequestFileCategoryRoleDto requestFileCategoryRoleDto){
        adminService.postFileCategoryRole(requestFileCategoryRoleDto);
    }

    /**
     * 파일 카테고리 삭제
     *
     * @param fileCategoryId
     */
    @DeleteMapping("fileCategory/{fileCategoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFileCategory(@PathVariable Long fileCategoryId) {
        adminService.deleteFileCategory(fileCategoryId);
    }

}
