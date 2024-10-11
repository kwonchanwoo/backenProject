package com.example.module.converter;

import com.example.module.entity.FileCategory;
import com.example.module.repository.file_category.FileCategoryRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileCategoryConverter implements
        org.springframework.core.convert.converter.Converter<String, FileCategory>,
        com.fasterxml.jackson.databind.util.Converter<String, FileCategory> {

    private final FileCategoryRepository repository;

    @Override
    public FileCategory convert(String id) {
        return repository.findByIdAndDeletedFalse(Long.valueOf(id)).orElseThrow(() -> new CommonException(ErrorCode.ACCESS_DENIED));
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(FileCategory.class);
    }
}
