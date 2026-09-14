package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * ListEmployeeResponse.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.dto.EmployeeListDTO;
import lombok.Data;
import java.util.List;

/**
 * DTO response cho API lấy danh sách nhân viên (GET /employee).
 * Format theo specification: {"code": "200", "totalRecords": n, "employees": [...]}.
 *
 * <p>Lưu ý:
 * - code là String (không phải Integer) theo spec
 * - totalRecords là tổng số bản ghi phẳng (bao gồm cả bản ghi trùng khi nhân viên có nhiều chứng chỉ)
 * - employees chứa danh sách EmployeeListDTO dạng phẳng (flat, không nested)</p>
 *
 * @author quynv
 */
@Data
public class ListEmployeeResponse {

    /** Mã HTTP status dạng String (ví dụ: "200") */
    private String code;

    /** Tổng số bản ghi (không tính phân trang) */
    private Long totalRecords;

    /** Danh sách nhân viên dạng phẳng */
    private List<EmployeeListDTO> employees;
}
