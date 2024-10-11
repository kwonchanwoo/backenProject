package com.example.module.repository.file;

import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.util.security.SecurityContextHelper;
import com.querydsl.core.BooleanBuilder;
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
public class FileCustomRepositoryImpl implements FileCustomRepository {
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
                        member.userId,
                        file.size
                ))
                .from(file)
                .join(file.fileCategory, fileCategory)
                .join(file.createdMember, member)
                .leftJoin(fileCategoryRole)
                .on(
                        member.id.eq(fileCategoryRole.fileCategoryRolePK.member.id),
                        fileCategory.id.eq(fileCategoryRole.fileCategoryRolePK.fileCategory.id)
                )
                .where(whereClause())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(jpaQueryFactory.
                select(file.count())
                .from(file)
                .join(file.fileCategory, fileCategory)
                .join(file.createdMember, member)
                .join(fileCategoryRole)
                .on(
                        member.id.eq(fileCategoryRole.fileCategoryRolePK.member.id),
                        fileCategory.id.eq(fileCategoryRole.fileCategoryRolePK.fileCategory.id)
                )
                .where(whereClause())
                .fetchOne()).orElse(0L);

        return new PageImpl<>(list, pageable, count);
    }

    private BooleanBuilder whereClause() {
        BooleanBuilder builder = new BooleanBuilder();

        if(!SecurityContextHelper.isAdmin()) { // 일반회원 체크
            builder.and(member.userId.eq(SecurityContextHelper.getPrincipal().getUserId())); // 본인이 등록한 파일만 조회
            builder.and(fileCategoryRole.deleted.isFalse());// 파일 권한 여부 체크
        }
        return builder.and(file.deleted.isFalse()).and(fileCategory.deleted.isFalse());
    }
}

