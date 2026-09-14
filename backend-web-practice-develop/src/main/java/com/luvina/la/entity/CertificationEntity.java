package com.luvina.la.entity;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationEntity.java, Aug 17, 2026 nvquy
 */

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "certifications")
@Data
public class CertificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id", unique = true, nullable = false)
    private Long certificationId;

    @Column(name = "certification_name", nullable = false)
    private String certificationName;

    @Column(name = "certification_level", nullable = false)
    private Integer certificationLevel;
}
