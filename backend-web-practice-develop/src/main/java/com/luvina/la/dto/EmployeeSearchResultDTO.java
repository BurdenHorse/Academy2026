package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeSearchResultDTO.java, Sep 08, 2026 nvquy
 */

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa kết quả tìm kiếm danh sách nhân viên từ tầng Service.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Tổng số bản ghi (không tính phân trang) */
    private Long totalRecords;

    /** Danh sách nhân viên dạng phẳng */
    private List<EmployeeListDTO> employees;
}
