package com.example.module.api.admin.controller;

import com.example.module.api.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/fileCategory/{fileCategory}")
    @ResponseStatus(HttpStatus.OK)
    public void addFileCategory(@PathVariable String fileCategory) {
        adminService.addFileCatregory(fileCategory);
    }
}
