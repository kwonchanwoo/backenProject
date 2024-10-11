package com.example.module.repository.file_category;

import com.example.module.api.file_category.dto.response.ResponseFileCategoryDto;
import com.example.module.util.security.SecurityContextHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.module.entity.QFile.file;
import static com.example.module.entity.QFileCategory.fileCategory;
import static com.example.module.entity.QFileCategoryRole.fileCategoryRole;
import static com.example.module.entity.QMember.member;

@RequiredArgsConstructor
public class FileCategoryCustomRepositoryImpl implements FileCategoryCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ResponseFileCategoryDto> getFileCategoryList() {

        return jpaQueryFactory.
                select(Projections.constructor(
                        ResponseFileCategoryDto.class,
                        fileCategory.id,
                        fileCategory.name
                ))
                .from(fileCategory)
                .join(fileCategory.createdMember, member)
                .leftJoin(fileCategoryRole)
                .on(
                        member.id.eq(fileCategoryRole.fileCategoryRolePK.member.id),
                        fileCategory.id.eq(fileCategoryRole.fileCategoryRolePK.fileCategory.id)
                )
                .where(whereClause())
                .fetch();
    }

    private BooleanBuilder whereClause() {
        BooleanBuilder builder = new BooleanBuilder();

        if(!SecurityContextHelper.isAdmin()) { // 일반회원 체크
            builder.and(fileCategory.deleted.isFalse());
            builder.and(fileCategoryRole.deleted.isFalse());
        }
        return builder;
    }
}

