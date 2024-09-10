package com.example.module.entity;


import com.example.module.util.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EntityListeners(value = {AuditingEntityListener.class})
@Table(name = "token")
@Where(clause = "deleted=false")
public class Token extends BaseEntity {
    @Column(name = "token_key", nullable = false, columnDefinition = "varchar(255) not null comment '회원 ID'")
    private String tokenKey;

    @Column(name = "token_value", nullable = false, columnDefinition = "varchar(255) not null comment 'refresh token'")
    private String tokenValue;
}
