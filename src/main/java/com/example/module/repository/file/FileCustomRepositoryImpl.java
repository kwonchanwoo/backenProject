package com.example.module.repository.file;

import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.module.entity.QFile.file;

@RequiredArgsConstructor
public class FileCustomRepositoryImpl implements FileCustomRepository{
        private final JPAQueryFactory jpaQueryFactory;

        @Override
        public Page<ResponseFileDto> getFileList(Map<String, Object> filters, Pageable pageable) {

            List<ResponseFileDto> list = jpaQueryFactory.
                    select(file)
                    .from(file)
                    .leftJoin(file.createdMember, QMember.member)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch()
                    .stream()
                    .map(ResponseFileDto::new)
                    .collect(Collectors.toList());

            Long count = Optional.ofNullable(jpaQueryFactory.
                    select(file.count())
                    .from(file)
                    .fetchOne()).orElse(0L);

            return new PageImpl<>(list,pageable,count);
        }
    }

