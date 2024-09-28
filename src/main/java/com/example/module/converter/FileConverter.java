package com.example.module.converter;

import com.example.module.entity.File;
import com.example.module.repository.file.FileRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileConverter implements
        org.springframework.core.convert.converter.Converter<String, File>,
        com.fasterxml.jackson.databind.util.Converter<String, File> {

    private final FileRepository repository;

    @Override
    public File convert(String id) {
        return repository.findById(Long.valueOf(id)).orElseThrow(() -> new CommonException(ErrorCode.ACCESS_DENIED));
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(File.class);
    }
}
