package com.example.module.api.admin.controller;

import com.example.module.api.admin.dto.RequestFileCategoryRole;
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
     * @param fileCategory
     */
    @PostMapping("/fileCategory/{fileCategory}")
    @ResponseStatus(HttpStatus.OK)
    public void postFileCategory(@PathVariable String fileCategory) {
        adminService.postFileCategory(fileCategory);
    }

    /**
     * 파일 카테고리 권한 추가
     *
     * @param requestFileCategoryRole
     */
    @PostMapping("/fileCategory/role")
    @ResponseStatus(HttpStatus.OK)
    public void postFileCategoryRole(@RequestBody RequestFileCategoryRole requestFileCategoryRole){
        adminService.postFileCategoryRole(requestFileCategoryRole);
    }
}
