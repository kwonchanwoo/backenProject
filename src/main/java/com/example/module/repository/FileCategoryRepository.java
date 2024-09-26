package com.example.module.repository;

import com.example.module.entity.FileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileCategoryRepository extends JpaRepository<FileCategory, Long> {


    Optional<FileCategory> findByNameAndIsEnabled(String name, boolean isEnabled);

    Optional<FileCategory> findByName(String name);

}