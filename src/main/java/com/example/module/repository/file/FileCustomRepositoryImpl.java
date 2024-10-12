package com.example.module.repository.file;

import com.example.module.api.file.dto.response.ResponseFileDto;
import com.example.module.util.security.SecurityContextHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
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

        filterSetting(filters);

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
                .orderBy(getOrderSpecifiers(pageable.getSort()).toArray(new OrderSpecifier[0]))
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

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            // 일반적인 필드 정렬
            PathBuilder<Object> entityPath = new PathBuilder<>(getType(property), getMetadata(property));
            orders.add(new OrderSpecifier(direction, getTarget(property, entityPath)));
        });
        return orders;
    }

    private Expression<?> getTarget(String property, PathBuilder<Object> entityPath) {
        Expression<?> target = null;

        switch (property) {
            case "fileName":
                target = file.originName;
                break;
            case "fileCategory":
                target = fileCategory.name;
                break;
            case "memberName":
                target = member.name;
                break;
            case "id":
            case "description":
            case "size":
            case "userId":
                target = entityPath.get(property);
                break;
        }
        return target;
    }

    public Class<?> getType(String property) {
        Class<?> type = null;
        switch (property) {
            case "id":
            case "fileName":
            case "description":
            case "size":
                type = file.getType();
                break;
            case "fileCategory":
                type = fileCategory.getType();
                break;
            case "memberName":
            case "userId":
                type = member.getType();
                break;
        }
        return type;
    }

    public PathMetadata getMetadata(String property) {
        PathMetadata metadata = null;
        switch (property) {
            case "id":
            case "fileName":
            case "description":
            case "size":
                metadata = file.getMetadata();
                break;
            case "fileCategory":
                metadata = fileCategory.getMetadata();
                break;
            case "memberName":
            case "userId":
                metadata = member.getMetadata();
                break;
        }
        return metadata;
    }

    private BooleanBuilder whereClause() {
        BooleanBuilder builder = new BooleanBuilder();

        if (!SecurityContextHelper.isAdmin()) { // 일반회원 체크
            builder.and(member.userId.eq(SecurityContextHelper.getPrincipal().getUserId())); // 본인이 등록한 파일만 조회
            builder.and(fileCategoryRole.deleted.isFalse());// 파일 권한 여부 체크
        }
        return builder.and(file.deleted.isFalse()).and(fileCategory.deleted.isFalse());
    }

    private void filterSetting(Map<String, Object> filters) {
        filters.remove("page");
        filters.remove("size");
        filters.remove("sort");
    }
}

