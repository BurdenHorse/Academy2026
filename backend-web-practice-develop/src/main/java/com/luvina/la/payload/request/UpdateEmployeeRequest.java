package com.luvina.la.payload.request;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * UpdateEmployeeRequest.java, Sep 10, 2026
 */

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload request cho API cập nhật thông tin nhân viên (PUT /employee).
 * Theo đặc tả kỹ thuật TKAPI_Tong_hop.md - Section 7 (Update Employee).
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {

    /** ID nhân viên cần cập nhật (bắt buộc) */
    private Long employeeId;

    /** Tên đăng nhập (Account name, bắt buộc, không được thay đổi so với DB) */
    private String employeeLoginId;

    /** Tên nhân viên (氏名) */
    private String employeeName;

    /** Tên Katakana (カタカナ氏名) */
    private String employeeNameKana;

    /** Ngày sinh (yyyy/MM/dd) */
    private String employeeBirthDate;

    /** Email nhân viên */
    private String employeeEmail;

    /** Số điện thoại nhân viên */
    private String employeeTelephone;

    /** Mật khẩu đăng nhập mới (tùy chọn, để trống nếu không đổi mật khẩu) */
    private String employeeLoginPassword;

    /** ID phòng ban */
    private Long departmentId;

    /** Danh sách chứng chỉ tiếng Nhật (nếu có) */
    private List<EmployeeCertificationRequest> certifications;
}
