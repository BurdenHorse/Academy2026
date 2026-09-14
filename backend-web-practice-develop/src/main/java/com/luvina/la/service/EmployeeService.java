package com.luvina.la.service;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeService.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.dto.AddEmployeeDTO;
import com.luvina.la.dto.DeleteEmployeeDTO;
import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeSearchResultDTO;
import com.luvina.la.dto.UpdateEmployeeDTO;
import com.luvina.la.payload.request.AddEmployeeRequest;
import com.luvina.la.payload.request.UpdateEmployeeRequest;

/**
 * Interface service xử lý nghiệp vụ liên quan đến nhân viên.
 * Tầng Service xử lý logic nghiệp vụ và trả về các đối tượng DTO cho Controller.
 *
 * @author quynv
 */
public interface EmployeeService {

    /**
     * Lấy danh sách nhân viên theo điều kiện tìm kiếm, sắp xếp và phân trang.
     *
     * @param employeeName       Tên nhân viên (LIKE %...%), null nếu không lọc theo tên
     * @param departmentId       ID phòng ban, null nếu không lọc theo phòng ban
     * @param ordEmployeeName    Thứ tự sắp xếp theo tên (ASC/DESC), null nếu không sort
     * @param ordCertificationName Thứ tự sắp xếp theo chứng chỉ (ASC/DESC), null nếu không sort
     * @param ordEndDate         Thứ tự sắp xếp theo ngày hết hạn chứng chỉ (ASC/DESC), null nếu không sort
     * @param offset             Vị trí bản ghi bắt đầu (0-based)
     * @param limit              Số bản ghi tối đa trả về
     * @return EmployeeSearchResultDTO chứa danh sách nhân viên dạng phẳng và tổng số bản ghi
     */
    EmployeeSearchResultDTO getEmployeeList(String employeeName, Long departmentId,
                                            String ordEmployeeName, String ordCertificationName,
                                            String ordEndDate, String sortPriority,
                                            int offset, int limit);

    /**
     * Thêm mới nhân viên và thông tin chứng chỉ tiếng Nhật (nếu có).
     *
     * @param request Payload chứa toàn bộ thông tin nhân viên cần tạo mới
     * @return AddEmployeeDTO chứa ID nhân viên vừa tạo
     */
    AddEmployeeDTO addEmployee(AddEmployeeRequest request);

    /**
     * Lấy thông tin chi tiết của một nhân viên theo ID.
     *
     * @param employeeId ID nhân viên cần lấy thông tin
     * @return EmployeeDetailDTO chứa toàn bộ thông tin chi tiết nhân viên và danh sách chứng chỉ
     */
    EmployeeDetailDTO getEmployeeDetail(Long employeeId);

    /**
     * Xóa thông tin nhân viên và các chứng chỉ liên quan.
     *
     * @param employeeId ID nhân viên cần xóa
     * @return DeleteEmployeeDTO chứa ID nhân viên vừa xóa
     */
     DeleteEmployeeDTO deleteEmployee(Long employeeId);

    /**
     * Cập nhật thông tin nhân viên và các chứng chỉ tiếng Nhật.
     *
     * @param request Payload chứa toàn bộ thông tin nhân viên cần cập nhật
     * @return UpdateEmployeeDTO chứa ID nhân viên vừa cập nhật
     */
    UpdateEmployeeDTO updateEmployee(UpdateEmployeeRequest request);
}
