package com.example.module.repository.member;

import com.example.module.api.member.dto.response.ResponseMemberDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.module.entity.QMember.member;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ResponseMemberDto> getMemberList(Map<String, Object> filters, Pageable pageable) {

        List<ResponseMemberDto> list = jpaQueryFactory.
                selectFrom(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(ResponseMemberDto::new)
                .collect(Collectors.toList());

        Long count = Optional.ofNullable(jpaQueryFactory.
                select(member.count())
                .from(member)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(list,pageable,count);
    }
}
