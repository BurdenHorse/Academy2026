package com.luvina.la.payload.request;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeCertificationRequest.java, Aug 26, 2026 nvquy
 */

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thông tin chứng chỉ đính kèm trong request tạo mới/cập nhật nhân viên.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCertificationRequest {

    /** ID chứng chỉ (bảng certifications) */
    private Long certificationId;

    /** Ngày cấp chứng chỉ dạng chuỗi (yyyy/MM/dd) */
    private String certificationStartDate;

    /** Ngày hết hạn chứng chỉ dạng chuỗi (yyyy/MM/dd) */
    private String certificationEndDate;

    /** Điểm số chứng chỉ */
    private BigDecimal employeeCertificationScore;
}
