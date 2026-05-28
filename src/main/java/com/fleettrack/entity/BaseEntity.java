package com.fleettrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false, nullable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "CREATED_BY", length = 20, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "UPDATED_AT", insertable = false)
    private Instant updatedAt;

    @LastModifiedBy
    @Column(name = "UPDATED_BY", length = 20, insertable = false)
    private String updatedBy;
}

