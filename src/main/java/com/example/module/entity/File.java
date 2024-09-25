package com.example.module.entity;

import com.example.module.util.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File extends BaseEntity {
    // 저장된 서버 파일명
    @Column(unique = true, nullable = false)
    private String storedName;
    // 사용자 업로드 파일명
    @Column(nullable = false)
    private String originName;
    // 파일 경로
    @Column(nullable = false)
    private String path;
    // 파일 설명
    @Column
    private String description;
    // 파일 크기
    @Column(nullable = false)
    private long size;
    // 다운로드 횟수
    @Column(nullable = false)
    @ColumnDefault("0")
    private int downloadCnt = 0;

    // foreign key for member table
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "file_category_id")
    private FileCategory fileCategory;

}
