package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeDetailDTO.java, Sep 08, 2026 nvquy
 */

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin chi tiết nhân viên và danh sách chứng chỉ tiếng Nhật từ tầng Service.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private String employeeName;
    private String employeeBirthDate;
    private Long departmentId;
    private String departmentName;
    private String employeeEmail;
    private String employeeTelephone;
    private String employeeNameKana;
    private String employeeLoginId;
    private List<EmployeeCertificationDetailDTO> certifications;
    private Integer role;

    /**
     * DTO chi tiết một chứng chỉ tiếng Nhật của nhân viên.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeCertificationDetailDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long certificationId;
        private String certificationName;
        private String startDate;
        private String endDate;
        private Integer score;
    }
}
