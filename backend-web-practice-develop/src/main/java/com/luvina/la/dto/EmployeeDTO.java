package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeDTO.java, Aug 17, 2026 nvquy
 */

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * DTO chứa thông tin nhân viên đầy đủ, dùng cho các thao tác CRUD.
 * Bao gồm thông tin cá nhân, phòng ban và danh sách chứng chỉ.
 *
 * @author quynv
 */
@Data
public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 6868189362900231672L;

    private Long employeeId;
    private String employeeName;
    private String employeeNameKana;
    private Date employeeBirthDate;
    private String employeeEmail;
    private String employeeTelephone;
    private String employeeLoginId;
    private Integer role;

    private Long departmentId;
    private String departmentName;

    private List<EmployeeCertificationDTO> certifications;
}
