package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationDTO.java, Aug 21, 2026 nvquy
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin chứng chỉ tiếng Nhật.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDTO {

    /** ID chứng chỉ dạng String */
    private String certificationId;

    /** Tên chứng chỉ tiếng Nhật */
    private String certificationName;

    /** Cấp độ chứng chỉ (1 đến 5) */
    private Integer certificationLevel;
}
