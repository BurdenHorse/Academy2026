package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeCertificationDTO.java, Aug 17, 2026 nvquy
 */


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * DTO chứa thông tin chứng chỉ tiếng Nhật của nhân viên.
 * Bao gồm mã chứng chỉ, tên, ngày cấp, ngày hết hạn và điểm thi.
 *
 * @author quynv
 */
@Data
public class EmployeeCertificationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long certificationId;
    private String certificationName;
    private Date certificationStartDate;
    private Date certificationEndDate;
    private Integer employeeCertificationScore;
}
