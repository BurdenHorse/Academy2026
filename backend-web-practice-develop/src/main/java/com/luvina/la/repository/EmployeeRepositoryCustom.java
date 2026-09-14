package com.luvina.la.repository;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeRepositoryCustom.java, Aug 22, 2026 nvquy
 */

import com.luvina.la.dto.EmployeeListDTO;
import java.util.List;

/**
 * Interface custom repository cho các truy vấn phức tạp liên quan đến nhân viên.
 * Hỗ trợ tìm kiếm theo tên/phòng ban, sắp xếp đa cột
 * (theo tên, chứng chỉ, ngày hết hạn) và phân trang theo offset/limit.
 *
 * @author quynv
 */
public interface EmployeeRepositoryCustom {

    /**
     * Tìm kiếm danh sách nhân viên theo điều kiện, sắp xếp và phân trang.
     * Kết quả trả về dạng phẳng (flat) — mỗi bản ghi là một cặp nhân viên-chứng chỉ.
     * Nhân viên không có chứng chỉ vẫn được trả về với các trường chứng chỉ null.
     *
     * @param employeeName       Tên nhân viên tìm kiếm (LIKE %...%), null nếu không lọc
     * @param departmentId       ID phòng ban cần lọc, null nếu không lọc
     * @param ordEmployeeName    Thứ tự sắp xếp theo tên (ASC/DESC), null nếu không sort
     * @param ordCertificationName Thứ tự sắp xếp theo tên chứng chỉ (ASC/DESC), null nếu không sort
     * @param ordEndDate         Thứ tự sắp xếp theo ngày hết hạn chứng chỉ (ASC/DESC), null nếu không sort
     * @param offset             Vị trí bản ghi bắt đầu (0-based)
     * @param limit              Số bản ghi tối đa trả về
     * @return Danh sách EmployeeListDTO dạng phẳng
     */
    List<EmployeeListDTO> searchEmployees(String employeeName, Long departmentId,
                                          String ordEmployeeName, String ordCertificationName,
                                          String ordEndDate, String sortPriority,
                                          int offset, int limit);

    /**
     * Đếm tổng số bản ghi nhân viên theo điều kiện tìm kiếm (bao gồm LEFT JOIN chứng chỉ).
     * Tổng số này bao gồm cả bản ghi trùng lặp khi nhân viên có nhiều chứng chỉ.
     *
     * @param employeeName Tên nhân viên tìm kiếm (LIKE %...%), null nếu không lọc
     * @param departmentId ID phòng ban cần lọc, null nếu không lọc
     * @return Tổng số bản ghi phẳng (flat records)
     */
    long countEmployees(String employeeName, Long departmentId);
}
