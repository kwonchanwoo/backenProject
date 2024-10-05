package com.example.module.repository.file;

import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.util.security.SecurityContextHelper;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.module.entity.QFile.file;
import static com.example.module.entity.QFileCategory.fileCategory;
import static com.example.module.entity.QFileCategoryRole.fileCategoryRole;
import static com.example.module.entity.QMember.member;

@RequiredArgsConstructor
public class FileCustomRepositoryImpl implements FileCustomRepository{
        private final JPAQueryFactory jpaQueryFactory;

        @Override
        public Page<ResponseFileDto> getFileList(Map<String, Object> filters, Pageable pageable) {

            List<ResponseFileDto> list = jpaQueryFactory.
                    select(Projections.constructor(
                        ResponseFileDto.class,
                        file.id,
                        file.originName,
                        fileCategory.name,
                        file.description,
                        member.name,
                        file.size
                    ))
                    .from(file)
                    .join(file.fileCategory,fileCategory)
                    .join(file.createdMember, member)
                    .join(fileCategoryRole)
                    .on(
                            member.id.eq(fileCategoryRole.fileCategoryRolePK.member.id),
                            fileCategory.id.eq(fileCategoryRole.fileCategoryRolePK.fileCategory.id)
                    )
                    .where(member.userId.eq(SecurityContextHelper.getPrincipal().getUserId()))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            Long count = Optional.ofNullable(jpaQueryFactory.
                    select(file.count())
                    .from(file)
                    .fetchOne()).orElse(0L);

            return new PageImpl<>(list,pageable,count);
        }
    }

