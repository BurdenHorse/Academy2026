package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeDetailResponse.java, Sep 08, 2026 nvquy
 */

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload response cho API lấy chi tiết thông tin nhân viên (GET /employee/{id}).
 * Theo đặc tả kỹ thuật TKAPI_Tong_hop.md - Section 5.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailResponse {

    /** Mã HTTP status dạng String (ví dụ: "200") */
    private String code;

    /** ID nhân viên */
    private Long employeeId;

    /** Họ tên nhân viên */
    private String employeeName;

    /** Ngày sinh nhân viên (định dạng yyyy/MM/dd) */
    private String employeeBirthDate;

    /** ID phòng ban */
    private Long departmentId;

    /** Tên phòng ban */
    private String departmentName;

    /** Địa chỉ email */
    private String employeeEmail;

    /** Số điện thoại */
    private String employeeTelephone;

    /** Tên Katakana */
    private String employeeNameKana;

    /** Tên đăng nhập tài khoản */
    private String employeeLoginId;

    /** Danh sách chứng chỉ tiếng Nhật (sắp xếp theo certification_level tăng dần) */
    private List<EmployeeCertificationDetailDTO> certifications;

    /** Vai trò người dùng (0: User, 1: Admin) */
    private Integer role;

    /**
     * DTO đại diện cho thông tin một chứng chỉ tiếng Nhật của nhân viên.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeCertificationDetailDTO {

        /** ID chứng chỉ */
        private Long certificationId;

        /** Tên chứng chỉ */
        private String certificationName;

        /** Ngày cấp chứng chỉ (định dạng yyyy/MM/dd) */
        private String startDate;

        /** Ngày hết hạn chứng chỉ (định dạng yyyy/MM/dd) */
        private String endDate;

        /** Điểm số chứng chỉ */
        private Integer score;
    }
}

