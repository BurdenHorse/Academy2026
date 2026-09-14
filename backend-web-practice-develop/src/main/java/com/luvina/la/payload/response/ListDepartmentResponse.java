package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * ListDepartmentResponse.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.dto.DepartmentDTO;
import lombok.Data;
import java.util.List;

/**
 * DTO response cho API lấy danh sách phòng ban (GET /departments).
 * Format theo specification: {"code": "200", "departments": [...]}.
 *
 * @author quynv
 */
@Data
public class ListDepartmentResponse {

    /** Mã HTTP status dạng String (ví dụ: "200") */
    private String code;

    /** Danh sách phòng ban */
    private List<DepartmentDTO> departments;
}
