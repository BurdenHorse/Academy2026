package com.luvina.la.controller;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentController.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.config.Constants;
import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.payload.response.ListDepartmentResponse;
import com.luvina.la.service.DepartmentService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller xử lý các request liên quan đến phòng ban.
 * Cung cấp API lấy danh sách phòng ban cho dropdown tìm kiếm trên ADM002.
 * Endpoint: /departments
 *
 * @author quynv
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Constructor injection cho DepartmentService.
     *
     * @param departmentService Service xử lý nghiệp vụ phòng ban
     */
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * API lấy danh sách tất cả phòng ban.
     * Endpoint: GET /departments (không có tham số).
     * Dùng để binding dữ liệu vào dropdown "Nhóm" trên màn hình ADM002.
     *
     * @return ListDepartmentResponse chứa danh sách phòng ban
     */
    @GetMapping
    public ListDepartmentResponse getDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        ListDepartmentResponse response = new ListDepartmentResponse();
        response.setCode(Constants.STATUS_CODE_SUCCESS);
        response.setDepartments(departments);
        return response;
    }
}
