package com.luvina.la.payload.request;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * AddEmployeeRequest.java, Aug 26, 2026 nvquy
 */

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload request cho API thêm mới nhân viên (POST /employee).
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeeRequest {

    /** Tên đăng nhập (Account name) */
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

    /** Mật khẩu đăng nhập */
    private String employeeLoginPassword;

    /** ID phòng ban */
    private Long departmentId;

    /** Danh sách chứng chỉ tiếng Nhật (nếu có) */
    private List<EmployeeCertificationRequest> certifications;
}
