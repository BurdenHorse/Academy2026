package com.luvina.la.controller;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentController.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.mapper.DepartmentMapper;
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
    private final DepartmentMapper departmentMapper;

    /**
     * Constructor injection cho DepartmentService và DepartmentMapper.
     *
     * @param departmentService Service xử lý nghiệp vụ phòng ban
     * @param departmentMapper  Mapper chuyển đổi dữ liệu phòng ban
     */
    public DepartmentController(DepartmentService departmentService, DepartmentMapper departmentMapper) {
        this.departmentService = departmentService;
        this.departmentMapper = departmentMapper;
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
        return departmentMapper.toListResponse(departments);
    }
}
