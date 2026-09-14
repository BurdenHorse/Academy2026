package com.luvina.la.service;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentService.java, Aug 22, 2026 nvquy
 */

import com.luvina.la.dto.DepartmentDTO;
import java.util.List;

/**
 * Interface service xử lý nghiệp vụ liên quan đến phòng ban.
 * Cung cấp phương thức lấy danh sách phòng ban cho dropdown tìm kiếm.
 *
 * @author quynv
 */
public interface DepartmentService {

    /**
     * Lấy danh sách tất cả phòng ban.
     * Dùng cho dropdown tìm kiếm theo phòng ban trên màn hình ADM002.
     *
     * @return Danh sách DepartmentDTO
     */
    List<DepartmentDTO> getAllDepartments();
}
