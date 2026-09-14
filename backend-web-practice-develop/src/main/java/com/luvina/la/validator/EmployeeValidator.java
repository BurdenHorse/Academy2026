package com.luvina.la.validator;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidator.java, Sep 07, 2026
 */

import com.luvina.la.config.Constants;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.exception.AppException;
import com.luvina.la.payload.request.AddEmployeeRequest;
import com.luvina.la.payload.request.EmployeeCertificationRequestDTO;
import com.luvina.la.payload.request.UpdateEmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component chuyên trách kiểm tra tính hợp lệ (Validation) của dữ liệu nhân viên
 * theo đúng đặc tả tài liệu TKAPI_Tong_hop.md và ADM002.md.
 */
@Component
public class EmployeeValidator {

    private static final List<String> VALID_SORT_VALUES = Arrays.asList(Constants.SORT_ASC, Constants.SORT_DESC);
    private static final Pattern LOGIN_ID_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    private static final Pattern KANA_PATTERN = Pattern.compile("^[\\uFF66-\\uFF9F\\s]+$");
    private static final Pattern HALFSIZE_PATTERN = Pattern.compile("^[0-9+() -]+$");
    private static final Pattern DATE_FORMAT_PATTERN = Pattern.compile("^\\d{4}/\\d{2}/\\d{2}$");

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CertificationRepository certificationRepository;

    /**
     * Validate tham số sort phải là ASC hoặc DESC.
     * Nếu giá trị khác ASC/DESC → ném AppException với mã lỗi ER021.
     * Nếu giá trị null hoặc rỗng → bỏ qua.
     *
     * @param value     Giá trị tham số sort
     * @param paramName Tên tham số (dùng cho thông tin lỗi)
     */
    public void validateSortParam(String value, String paramName) {
        if (value != null && !value.trim().isEmpty()) {
            String upperValue = value.trim().toUpperCase();
            if (!VALID_SORT_VALUES.contains(upperValue)) {
                throw new AppException(Constants.ERROR_CODE_SORT_INVALID, Collections.singletonList(paramName));
            }
        }
    }

    /**
     * Parse chuỗi thành số nguyên dương (>= 0).
     * Nếu chuỗi null hoặc rỗng → trả về giá trị mặc định.
     * Nếu không phải số nguyên dương → ném AppException với mã lỗi ER018.
     *
     * @param value        Chuỗi cần parse
     * @param defaultValue Giá trị mặc định nếu chuỗi rỗng hoặc null
     * @param paramNameJp  Tên tham số bằng tiếng Nhật (dùng cho message lỗi ER018)
     * @return Số nguyên đã parse hoặc giá trị mặc định
     */
    public int parsePositiveIntParam(String value, int defaultValue, String paramNameJp) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed < 0) {
                throw new AppException(Constants.ERROR_CODE_HALFSIZE_NUMBER, Collections.singletonList(paramNameJp));
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new AppException(Constants.ERROR_CODE_HALFSIZE_NUMBER, Collections.singletonList(paramNameJp));
        }
    }

    /**
     * Parse departmentId từ String sang Long.
     * Trả về null nếu giá trị rỗng hoặc không hợp lệ (bỏ qua điều kiện lọc).
     *
     * @param departmentId Chuỗi departmentId cần parse
     * @return Long đã parse hoặc null nếu không hợp lệ
     */
    public Long parseDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(departmentId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Validate độ dài tên nhân viên khi tìm kiếm (tối đa 125 ký tự).
     *
     * @param employeeName Tên nhân viên tìm kiếm
     */
    public void validateSearchEmployeeName(String employeeName) {
        if (employeeName != null && employeeName.length() > Constants.MAX_LENGTH_EMPLOYEE_NAME) {
            throw new AppException(Constants.ERROR_CODE_MAX_LENGTH,
                    Arrays.asList(Constants.PARAM_NAME_EMPLOYEE_NAME, String.valueOf(Constants.MAX_LENGTH_EMPLOYEE_NAME)));
        }
    }

    /**
     * Validate và parse ID nhân viên từ chuỗi sang Long.
     * Kiểm tra ID không rỗng và phải là số nguyên dương nửa byte (halfsize number).
     *
     * @param id Chuỗi ID nhân viên
     * @return Long ID đã parse
     */
    public Long validateAndParseId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_ID));
        }
        try {
            long parsed = Long.parseLong(id.trim());
            if (parsed <= 0) {
                throw new AppException(Constants.ERROR_CODE_HALFSIZE_NUMBER, Collections.singletonList(Constants.PARAM_NAME_ID));
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new AppException(Constants.ERROR_CODE_HALFSIZE_NUMBER, Collections.singletonList(Constants.PARAM_NAME_ID));
        }
    }

    /**
     * Validate request xóa nhân viên:
     * - Validate ID hợp lệ (ER001, ER018)
     * - Kiểm tra nhân viên tồn tại trong hệ thống (ER014)
     * - Nếu là tài khoản Admin (role = 1), từ chối xóa và ném ER020
     *
     * @param idStr Chuỗi ID nhân viên cần xóa
     * @return EmployeeEntity thực thể nhân viên tìm thấy từ DB
     */
    public EmployeeEntity validateDeleteRequest(String idStr) {
        Long employeeId = validateAndParseId(idStr);
        EmployeeEntity employeeEntity = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new AppException(Constants.ERROR_CODE_USER_NOT_FOUND_DELETE, Collections.emptyList()));

        if (employeeEntity.getRole() != null && employeeEntity.getRole() == Constants.ROLE_ADMIN) {
            throw new AppException(Constants.ERROR_CODE_CANNOT_DELETE_ADMIN, Collections.emptyList());
        }

        return employeeEntity;
    }

    /**
     * Validate toàn bộ dữ liệu request thêm mới nhân viên (POST /employee).
     * Nếu có bất kỳ vi phạm nào, ném AppException chứa mã lỗi chuẩn.
     *
     * @param request AddEmployeeRequest
     */
    public void validateAddRequest(AddEmployeeRequest request) {
        validateEmployeeLoginId(request.getEmployeeLoginId());
        validateEmployeeName(request.getEmployeeName());
        validateEmployeeNameKana(request.getEmployeeNameKana());
        validateEmployeeBirthDate(request.getEmployeeBirthDate());
        validateEmployeeEmail(request.getEmployeeEmail());
        validateEmployeeTelephone(request.getEmployeeTelephone());
        validateEmployeeLoginPassword(request.getEmployeeLoginPassword());
        validateDepartmentId(request.getDepartmentId());
        validateCertifications(request.getCertifications());
    }

    /**
     * Validate toàn bộ dữ liệu request cập nhật nhân viên (PUT /employee).
     * Kiểm tra employeeId tồn tại, employeeLoginId phải khớp chính xác với DB (không cho phép sửa),
     * mật khẩu không bắt buộc (nếu không nhập thì giữ nguyên).
     *
     * @param request UpdateEmployeeRequest
     * @return EmployeeEntity thực thể nhân viên tìm thấy từ DB
     */
    public EmployeeEntity validateUpdateRequest(UpdateEmployeeRequest request) {
        if (request.getEmployeeId() == null) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_ID));
        }
        if (request.getEmployeeId() <= 0) {
            throw new AppException(Constants.ERROR_CODE_HALFSIZE_NUMBER, Collections.singletonList(Constants.PARAM_NAME_ID));
        }
        EmployeeEntity existingEmployee = employeeRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new AppException(Constants.ERROR_CODE_USER_NOT_FOUND_GET, Collections.singletonList(Constants.PARAM_NAME_ID)));

        // Validate employeeLoginId: Bắt buộc, và phải giống chính xác giá trị trong DB của nhân viên này
        if (request.getEmployeeLoginId() == null || request.getEmployeeLoginId().trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_ACCOUNT));
        }
        if (!existingEmployee.getEmployeeLoginId().equals(request.getEmployeeLoginId().trim())) {
            throw new AppException(Constants.ERROR_CODE_ALREADY_EXISTS, Collections.singletonList(Constants.PARAM_NAME_ACCOUNT));
        }

        validateEmployeeName(request.getEmployeeName());
        validateEmployeeNameKana(request.getEmployeeNameKana());
        validateEmployeeBirthDate(request.getEmployeeBirthDate());
        validateEmployeeEmailForUpdate(request.getEmployeeEmail());
        validateEmployeeTelephone(request.getEmployeeTelephone());
        validateEmployeeLoginPasswordForUpdate(request.getEmployeeLoginPassword());
        validateDepartmentId(request.getDepartmentId());
        validateCertifications(request.getCertifications());

        return existingEmployee;
    }

    /**
     * Validate email cho trường hợp update: bắt buộc nhập, max 125 ký tự.
     *
     * @param email Email nhân viên
     */
    public void validateEmployeeEmailForUpdate(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_EMAIL));
        }
        String trimmedEmail = email.trim();
        if (trimmedEmail.length() > 125) {
            throw new AppException(Constants.ERROR_CODE_MAX_LENGTH, Collections.singletonList(Constants.PARAM_NAME_EMAIL));
        }
    }

    /**
     * Validate password cho trường hợp update: nếu có nhập thì validate độ dài 8-50 ký tự, nếu null/rỗng thì cho phép bỏ qua.
     *
     * @param password Mật khẩu mới (tùy chọn)
     */
    public void validateEmployeeLoginPasswordForUpdate(String password) {
        if (password != null && !password.trim().isEmpty()) {
            String trimmedPassword = password.trim();
            if (trimmedPassword.length() < 8 || trimmedPassword.length() > 50) {
                throw new AppException(Constants.ERROR_CODE_LENGTH_RANGE, Arrays.asList(Constants.PARAM_NAME_PASSWORD, "8", "50"));
            }
        }
    }

    /**
     * Validate 1.1: [employeeLoginId]
     */
    public void validateEmployeeLoginId(String loginId) {
        if (loginId == null || loginId.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_ACCOUNT));
        }
        String trimmedLoginId = loginId.trim();
        if (trimmedLoginId.length() > 50) {
            throw new AppException(Constants.ERROR_CODE_MAX_LENGTH, Collections.singletonList(Constants.PARAM_NAME_ACCOUNT));
        }
        if (!LOGIN_ID_PATTERN.matcher(trimmedLoginId).matches() || Character.isDigit(trimmedLoginId.charAt(0))) {
            throw new AppException(Constants.ERROR_CODE_LOGIN_ID_FORMAT, Collections.singletonList(Constants.PARAM_NAME_ACCOUNT));
        }
        if (employeeRepository.existsByEmployeeLoginId(trimmedLoginId)) {
            throw new AppException(Constants.ERROR_CODE_ALREADY_EXISTS, Collections.singletonList(Constants.PARAM_NAME_ACCOUNT));
        }
    }

    /**
     * Validate 1.2: [employeeName]
     */
    public void validateEmployeeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_EMPLOYEE_NAME));
        }
        if (name.trim().length() > 125) {
            throw new AppException(Constants.ERROR_CODE_MAX_LENGTH, Collections.singletonList(Constants.PARAM_NAME_EMPLOYEE_NAME));
        }
    }

    /**
     * Validate 1.3: [employeeNameKana] (chỉ nhận halfsize Katakana)
     */
    public void validateEmployeeNameKana(String nameKana) {
        if (nameKana == null || nameKana.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_FULL_NAME_KANA));
        }
        String trimmedNameKana = nameKana.trim();
        if (trimmedNameKana.length() > 125) {
            throw new AppException(Constants.ERROR_CODE_MAX_LENGTH, Collections.singletonList(Constants.PARAM_NAME_FULL_NAME_KANA));
        }
        if (!KANA_PATTERN.matcher(trimmedNameKana).matches()) {
            throw new AppException(Constants.ERROR_CODE_KANA, Collections.singletonList(Constants.PARAM_NAME_FULL_NAME_KANA));
        }
    }

    /**
     * Validate 1.4: [employeeBirthDate]
     */
    public Date validateEmployeeBirthDate(String birthDateStr) {
        if (birthDateStr == null || birthDateStr.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_BIRTH_DATE));
        }
        return parseDate(birthDateStr.trim(), Constants.PARAM_NAME_BIRTH_DATE);
    }

    /**
     * Validate 1.5: [employeeEmail]
     */
    public void validateEmployeeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_EMAIL));
        }
        String trimmedEmail = email.trim();
        if (trimmedEmail.length() > 125) {
            throw new AppException(Constants.ERROR_CODE_MAX_LENGTH, Collections.singletonList(Constants.PARAM_NAME_EMAIL));
        }
    }

    /**
     * Validate 1.6: [employeeTelephone]
     */
    public void validateEmployeeTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_TELEPHONE));
        }
        String trimmedTelephone = telephone.trim();
        if (trimmedTelephone.length() > 50) {
            throw new AppException(Constants.ERROR_CODE_MAX_LENGTH, Collections.singletonList(Constants.PARAM_NAME_TELEPHONE));
        }
        if (!HALFSIZE_PATTERN.matcher(trimmedTelephone).matches()) {
            throw new AppException(Constants.ERROR_CODE_HALFSIZE, Collections.singletonList(Constants.PARAM_NAME_TELEPHONE));
        }
    }

    /**
     * Validate 1.7: [employeeLoginPassword]
     */
    public void validateEmployeeLoginPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_PASSWORD));
        }
        String trimmedPassword = password.trim();
        if (trimmedPassword.length() < 8 || trimmedPassword.length() > 50) {
            throw new AppException(Constants.ERROR_CODE_LENGTH_RANGE, Arrays.asList(Constants.PARAM_NAME_PASSWORD, "8", "50"));
        }
    }

    /**
     * Validate 1.8: [departmentId]
     */
    public void validateDepartmentId(Long departmentId) {
        if (departmentId == null) {
            throw new AppException(Constants.ERROR_CODE_SELECT_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_GROUP));
        }
        if (departmentId <= 0) {
            throw new AppException(Constants.ERROR_CODE_HALFSIZE_NUMBER, Collections.singletonList(Constants.PARAM_NAME_GROUP));
        }
        if (!departmentRepository.existsById(departmentId)) {
            throw new AppException(Constants.ERROR_CODE_NOT_FOUND, Collections.singletonList(Constants.PARAM_NAME_GROUP));
        }
    }

    /**
     * Validate 1.9: [certifications]
     */
    public void validateCertifications(List<EmployeeCertificationRequestDTO> certs) {
        if (certs == null || certs.isEmpty()) {
            return;
        }

        for (EmployeeCertificationRequestDTO certDTO : certs) {
            // certificationId
            Long certificationId = certDTO.getCertificationId();
            if (certificationId == null) {
                throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_CERTIFICATION));
            }
            if (certificationId <= 0) {
                throw new AppException(Constants.ERROR_CODE_HALFSIZE_NUMBER, Collections.singletonList(Constants.PARAM_NAME_CERTIFICATION));
            }
            if (!certificationRepository.existsById(certificationId)) {
                throw new AppException(Constants.ERROR_CODE_NOT_FOUND, Collections.singletonList(Constants.PARAM_NAME_CERTIFICATION));
            }

            // startDate
            String startDateStr = certDTO.getCertificationStartDate();
            if (startDateStr == null || startDateStr.trim().isEmpty()) {
                throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_CERT_START_DATE));
            }
            Date startDate = parseDate(startDateStr.trim(), Constants.PARAM_NAME_CERT_START_DATE);

            // endDate
            String endDateStr = certDTO.getCertificationEndDate();
            if (endDateStr == null || endDateStr.trim().isEmpty()) {
                throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_CERT_END_DATE));
            }
            Date endDate = parseDate(endDateStr.trim(), Constants.PARAM_NAME_CERT_END_DATE);

            // certificationEndDate <= certificationStartDate -> ER012
            if (!endDate.after(startDate)) {
                throw new AppException(Constants.ERROR_CODE_END_DATE_INVALID, Collections.emptyList());
            }

            // score
            BigDecimal score = certDTO.getEmployeeCertificationScore();
            if (score == null) {
                throw new AppException(Constants.ERROR_CODE_REQUIRED, Collections.singletonList(Constants.PARAM_NAME_SCORE));
            }
            if (score.compareTo(BigDecimal.ZERO) < 0) {
                throw new AppException(Constants.ERROR_CODE_HALFSIZE_NUMBER, Collections.singletonList(Constants.PARAM_NAME_SCORE));
            }
        }
    }

    /**
     * Parse chuỗi ngày tháng dạng yyyy/MM/dd.
     * Phân biệt rõ:
     * - Sai định dạng yyyy/MM/dd -> ném mã ER005
     * - Đúng định dạng nhưng ngày không hợp lệ trên lịch (vd: 2023/02/30) -> ném mã ER011
     *
     * @param dateStr   Chuỗi ngày tháng
     * @param fieldName Tên trường tiếng Nhật để ném mã lỗi
     * @return Date hợp lệ
     */
    public Date parseDate(String dateStr, String fieldName) {
        if (!DATE_FORMAT_PATTERN.matcher(dateStr).matches()) {
            throw new AppException(Constants.ERROR_CODE_FORMAT_INVALID, Arrays.asList(fieldName, "yyyy/MM/dd"));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new AppException(Constants.ERROR_CODE_DATE_INVALID, Collections.singletonList(fieldName));
        }
    }
}

