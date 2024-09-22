package com.example.module.entity;

import com.example.module.util.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File extends BaseEntity {
    // UUID
    @Column(unique = true, nullable = false)
    private String uuid;
    // 파일 경로
    @Column(nullable = false)
    private String path;
    // 파일명
    @Column(nullable = false)
    private String name;
    // 파일 설명
    @Column
    private String description;
    // 파일 크기
    @Column(nullable = false)
    private long size;
    // 다운로드 횟수
    @Column
    private int downloadCnt = 0;

    // foreign key for member table
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

}
