package com.luvina.la.controller;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeController.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.config.Constants;
import com.luvina.la.dto.AddEmployeeDTO;
import com.luvina.la.dto.DeleteEmployeeDTO;
import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeSearchResultDTO;
import com.luvina.la.dto.UpdateEmployeeDTO;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.request.AddEmployeeRequest;
import com.luvina.la.payload.request.UpdateEmployeeRequest;
import com.luvina.la.payload.response.AddEmployeeResponse;
import com.luvina.la.payload.response.DeleteEmployeeResponse;
import com.luvina.la.payload.response.EmployeeDetailResponse;
import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.payload.response.UpdateEmployeeResponse;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.util.StringUtil;
import com.luvina.la.validator.EmployeeValidator;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các request liên quan đến nhân viên.
 * Cung cấp API tìm kiếm, sắp xếp đa cột và phân trang danh sách nhân viên.
 * Endpoint: /employee
 *
 * @author quynv
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeValidator employeeValidator;
    private final EmployeeMapper employeeMapper;

    /**
     * Constructor injection cho EmployeeService, EmployeeValidator và EmployeeMapper.
     *
     * @param employeeService Service xử lý nghiệp vụ nhân viên
     * @param employeeValidator Validator kiểm tra tính hợp lệ dữ liệu
     * @param employeeMapper Mapper chuyển đổi dữ liệu response
     */
    public EmployeeController(EmployeeService employeeService,
                              EmployeeValidator employeeValidator,
                              EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeValidator = employeeValidator;
        this.employeeMapper = employeeMapper;
    }

    /**
     * API lấy danh sách nhân viên với chức năng tìm kiếm, sắp xếp và phân trang.
     * Endpoint: GET /employee
     *
     * <p>Các tham số tìm kiếm:
     * - employee_name: tìm gần đúng LIKE %...% (không bắt buộc)
     * - department_id: lọc theo phòng ban (không bắt buộc)
     *
     * <p>Các tham số sắp xếp (ASC/DESC, validate theo ER021):
     * - ord_employee_name: sắp xếp theo tên nhân viên
     * - ord_certification_name: sắp xếp theo tên chứng chỉ
     * - ord_end_date: sắp xếp theo ngày hết hạn chứng chỉ
     *
     * <p>Phân trang:
     * - offset: vị trí bản ghi bắt đầu (mặc định 0)
     * - limit: số bản ghi tối đa (mặc định 5)
     *
     * @param employeeName       Tên nhân viên tìm kiếm (không bắt buộc, max 125 ký tự)
     * @param departmentId       ID phòng ban lọc (không bắt buộc)
     * @param ordEmployeeName    Thứ tự sắp xếp theo tên nhân viên (ASC/DESC)
     * @param ordCertificationName Thứ tự sắp xếp theo tên chứng chỉ (ASC/DESC)
     * @param ordEndDate         Thứ tự sắp xếp theo ngày hết hạn chứng chỉ (ASC/DESC)
     * @param offset             Vị trí bản ghi bắt đầu (mặc định 0)
     * @param limit              Số bản ghi tối đa trả về (mặc định 5)
     * @return ListEmployeeResponse chứa danh sách nhân viên và tổng số bản ghi
     */
    @GetMapping
    public ListEmployeeResponse listEmployees(
            @RequestParam(name = "employee_name", required = false) String employeeName,
            @RequestParam(name = "department_id", required = false) String departmentId,
            @RequestParam(name = "ord_employee_name", required = false) String ordEmployeeName,
            @RequestParam(name = "ord_certification_name", required = false) String ordCertificationName,
            @RequestParam(name = "ord_end_date", required = false) String ordEndDate,
            @RequestParam(name = "sort_priority", required = false) String sortPriority,
            @RequestParam(name = "offset", required = false) String offset,
            @RequestParam(name = "limit", required = false) String limit) {

        // Validate tham số sort — nếu có giá trị mà khác ASC/DESC → lỗi ER021
        employeeValidator.validateSortParam(ordEmployeeName, Constants.PARAM_ORD_EMPLOYEE_NAME);
        employeeValidator.validateSortParam(ordCertificationName, Constants.PARAM_ORD_CERTIFICATION_NAME);
        employeeValidator.validateSortParam(ordEndDate, Constants.PARAM_ORD_END_DATE);

        // Parse và validate offset — phải là số nguyên dương, mặc định 0
        int parsedOffset = employeeValidator.parsePositiveIntParam(offset, Constants.DEFAULT_OFFSET, Constants.PARAM_NAME_OFFSET);

        // Parse và validate limit — phải là số nguyên dương, mặc định 5
        int parsedLimit = employeeValidator.parsePositiveIntParam(limit, Constants.DEFAULT_LIMIT, Constants.PARAM_NAME_LIMIT);

        // Parse departmentId sang Long, bỏ qua nếu không hợp lệ
        Long parsedDepartmentId = StringUtil.toLongOrNull(departmentId);

        // Xử lý employeeName: trim và kiểm tra rỗng
        String trimmedName = StringUtil.trimToNull(employeeName);

        EmployeeSearchResultDTO resultDTO = employeeService.getEmployeeList(
                trimmedName, parsedDepartmentId,
                StringUtil.normalizeSort(ordEmployeeName),
                StringUtil.normalizeSort(ordCertificationName),
                StringUtil.normalizeSort(ordEndDate),
                sortPriority,
                parsedOffset,
                parsedLimit);

        return employeeMapper.toListResponse(resultDTO);
    }

    /**
     * API thêm mới nhân viên và thông tin chứng chỉ tiếng Nhật (nếu có).
     * Endpoint: POST /employee
     *
     * @param request Payload chứa thông tin nhân viên cần tạo mới
     * @return AddEmployeeResponse chứa mã code và ID nhân viên vừa tạo
     */
    @PostMapping
    public AddEmployeeResponse addEmployee(
            @RequestBody AddEmployeeRequest request) {
        employeeValidator.validateAddRequest(request);
        AddEmployeeDTO addEmployeeDTO = employeeService.addEmployee(request);
        return AddEmployeeResponse.success(addEmployeeDTO.getEmployeeId());
    }

    /**
     * API cập nhật thông tin nhân viên.
     * Endpoint: PUT /employee
     *
     * @param request Payload chứa thông tin nhân viên cần cập nhật
     * @return UpdateEmployeeResponse chứa mã code, MSG002 và ID nhân viên vừa cập nhật
     */
    @PutMapping
    public UpdateEmployeeResponse updateEmployee(
            @RequestBody UpdateEmployeeRequest request) {
        employeeValidator.validateUpdateRequest(request);
        UpdateEmployeeDTO updateEmployeeDTO = employeeService.updateEmployee(request);
        return UpdateEmployeeResponse.success(updateEmployeeDTO.getEmployeeId());
    }

    /**
     * API lấy chi tiết thông tin nhân viên theo ID.
     * Endpoint: GET /employee/{id}
     *
     * @param id ID nhân viên cần lấy chi tiết
     * @return EmployeeDetailResponse chứa thông tin chi tiết nhân viên
     */
    @GetMapping("/{id}")
    public EmployeeDetailResponse getEmployeeDetail(@PathVariable("id") String id) {
        Long employeeId = employeeValidator.validateAndParseId(id);
        EmployeeDetailDTO employeeDetailDTO = employeeService.getEmployeeDetail(employeeId);
        return employeeMapper.toDetailResponse(employeeDetailDTO);
    }

    /**
     * API xóa nhân viên theo ID.
     * Endpoint: DELETE /employee/{id}
     *
     * @param id ID nhân viên cần xóa
     * @return DeleteEmployeeResponse chứa mã MSG003 và ID nhân viên vừa xóa
     */
    @DeleteMapping("/{id}")
    public DeleteEmployeeResponse deleteEmployee(@PathVariable("id") String id) {
        EmployeeEntity employeeEntity = employeeValidator.validateDeleteRequest(id);
        DeleteEmployeeDTO deleteEmployeeDTO = employeeService.deleteEmployee(employeeEntity.getEmployeeId());
        return DeleteEmployeeResponse.success(deleteEmployeeDTO.getEmployeeId());
    }
}

