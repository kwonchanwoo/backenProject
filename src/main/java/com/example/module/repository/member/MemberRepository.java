package com.example.module.repository.member;


import com.example.module.api.member.dto.response.ResponseMemberDto;
import com.example.module.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository{
    Optional<Member> findByEmail(String email);

    Page<ResponseMemberDto> getMemberList(Map<String, Object> filters, Pageable pageable);
}
