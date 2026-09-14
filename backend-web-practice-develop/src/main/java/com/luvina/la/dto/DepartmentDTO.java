package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentDTO.java, Aug 21, 2026 nvquy
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO lưu thông tin phòng ban cho response API List departments.
 * Các trường ID sử dụng kiểu String theo đúng specification API response.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    /** ID phòng ban (String theo specification API) */
    private String departmentId;

    /** Tên phòng ban */
    private String departmentName;
}