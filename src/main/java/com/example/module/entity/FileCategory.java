package com.example.module.entity;

import com.example.module.util.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder // 부모 클래스의 값을 builder 할수있음
@AllArgsConstructor
@NoArgsConstructor
public class FileCategory extends BaseEntity {
    @Column(nullable = false,unique = true)
    private String name;

    @Column(name = "enabled", columnDefinition = "bit(1) NOT NULL DEFAULT 1")
    @Builder.Default // @Builder 사용시 default 설정을할 때, 필요
    private boolean isEnabled = true;

    @OneToMany(mappedBy = "fileCategory", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

}
