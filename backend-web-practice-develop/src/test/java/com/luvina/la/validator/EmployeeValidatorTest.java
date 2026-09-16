package com.luvina.la.validator;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeValidatorTest.java, Sep 07, 2026
 */

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.exception.AppException;
import com.luvina.la.payload.request.AddEmployeeRequest;
import com.luvina.la.payload.request.EmployeeCertificationRequest;
import com.luvina.la.payload.request.UpdateEmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeValidatorTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CertificationRepository certificationRepository;

    @InjectMocks
    private EmployeeValidator employeeValidator;

    private AddEmployeeRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new AddEmployeeRequest();
        validRequest.setEmployeeLoginId("test_user");
        validRequest.setEmployeeName("Nguyễn Văn A");
        validRequest.setEmployeeNameKana("ｸﾞｴﾝ ｳﾞｧﾝ ｴｰ");
        validRequest.setEmployeeBirthDate("1995/05/15");
        validRequest.setEmployeeEmail("test@luvina.net");
        validRequest.setEmployeeTelephone("0123456789");
        validRequest.setEmployeeLoginPassword("password123");
        validRequest.setDepartmentId(1L);
    }

    @Test
    @DisplayName("Thêm nhân viên hợp lệ không có chứng chỉ -> không ném ngoại lệ")
    void testValidateAddRequest_SuccessWithoutCert() {
        when(employeeRepository.existsByEmployeeLoginId("test_user")).thenReturn(false);
        when(departmentRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> employeeValidator.validateAddRequest(validRequest));
    }

    @Test
    @DisplayName("Thêm nhân viên hợp lệ có chứng chỉ tiếng Nhật -> không ném ngoại lệ")
    void testValidateAddRequest_SuccessWithCert() {
        when(employeeRepository.existsByEmployeeLoginId("test_user")).thenReturn(false);
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(certificationRepository.existsById(2L)).thenReturn(true);

        EmployeeCertificationRequest cert = new EmployeeCertificationRequest();
        cert.setCertificationId(2L);
        cert.setCertificationStartDate("2023/01/01");
        cert.setCertificationEndDate("2024/01/01");
        cert.setEmployeeCertificationScore(new BigDecimal("95"));
        validRequest.setCertifications(Collections.singletonList(cert));

        assertDoesNotThrow(() -> employeeValidator.validateAddRequest(validRequest));
    }

    @Test
    @DisplayName("Tên đăng nhập để trống -> ném ER001")
    void testValidateLoginId_Empty_ThrowsER001() {
        validRequest.setEmployeeLoginId("");
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER001", ex.getErrorCode());
        assertEquals("アカウント名", ex.getParams().get(0));
    }

    @Test
    @DisplayName("Tên đăng nhập quá 50 ký tự -> ném ER006")
    void testValidateLoginId_TooLong_ThrowsER006() {
        validRequest.setEmployeeLoginId("a".repeat(51));
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER006", ex.getErrorCode());
    }

    @Test
    @DisplayName("Tên đăng nhập bắt đầu bằng số -> ném ER019")
    void testValidateLoginId_StartsWithDigit_ThrowsER019() {
        validRequest.setEmployeeLoginId("1testuser");
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER019", ex.getErrorCode());
    }

    @Test
    @DisplayName("Tên đăng nhập đã tồn tại trong DB -> ném ER003")
    void testValidateLoginId_Duplicate_ThrowsER003() {
        when(employeeRepository.existsByEmployeeLoginId("test_user")).thenReturn(true);

        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER003", ex.getErrorCode());
    }

    @Test
    @DisplayName("Tên Katakana không đúng ký tự Kana -> ném ER009")
    void testValidateKana_Invalid_ThrowsER009() {
        validRequest.setEmployeeNameKana("Nguyen Van A");
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER009", ex.getErrorCode());
    }

    @Test
    @DisplayName("Tên Katakana toàn giác (Full-width) -> ném ER008")
    void testValidateKana_Fullwidth_ThrowsER008() {
        validRequest.setEmployeeNameKana("グエン ヴァン エー");
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER008", ex.getErrorCode());
        assertEquals("カタカナ氏名", ex.getParams().get(0));
    }

    @Test
    @DisplayName("Ngày sinh sai format yyyy/MM/dd -> ném ER005")
    void testValidateBirthDate_WrongFormat_ThrowsER005() {
        validRequest.setEmployeeBirthDate("1995-05-15");
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER005", ex.getErrorCode());
        assertEquals("生年月日", ex.getParams().get(0));
    }

    @Test
    @DisplayName("Ngày sinh sai lịch (2023/02/30) -> ném ER011")
    void testValidateBirthDate_InvalidCalendar_ThrowsER011() {
        validRequest.setEmployeeBirthDate("2023/02/30");
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER011", ex.getErrorCode());
        assertEquals("生年月日", ex.getParams().get(0));
    }

    @Test
    @DisplayName("Email rỗng -> ném ER001")
    void testValidateEmail_Empty_ThrowsER001() {
        when(employeeRepository.existsByEmployeeLoginId("test_user")).thenReturn(false);
        validRequest.setEmployeeEmail("");

        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER001", ex.getErrorCode());
        assertEquals("メールアドレス", ex.getParams().get(0));
    }

    @Test
    @DisplayName("Mật khẩu dưới 8 ký tự -> ném ER007")
    void testValidatePassword_TooShort_ThrowsER007() {
        validRequest.setEmployeeLoginPassword("1234567");
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER007", ex.getErrorCode());
    }

    @Test
    @DisplayName("Phòng ban không tồn tại trong DB -> ném ER004")
    void testValidateDepartment_NotFound_ThrowsER004() {
        when(employeeRepository.existsByEmployeeLoginId("test_user")).thenReturn(false);
        when(departmentRepository.existsById(999L)).thenReturn(false);

        validRequest.setDepartmentId(999L);
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER004", ex.getErrorCode());
        assertEquals("グループ", ex.getParams().get(0));
    }

    @Test
    @DisplayName("Ngày hết hạn chứng chỉ nhỏ hơn ngày cấp -> ném ER012")
    void testValidateCert_EndDateBeforeStartDate_ThrowsER012() {
        when(employeeRepository.existsByEmployeeLoginId("test_user")).thenReturn(false);
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(certificationRepository.existsById(1L)).thenReturn(true);

        EmployeeCertificationRequest cert = new EmployeeCertificationRequest();
        cert.setCertificationId(1L);
        cert.setCertificationStartDate("2024/01/01");
        cert.setCertificationEndDate("2023/01/01");
        cert.setEmployeeCertificationScore(new BigDecimal("100"));
        validRequest.setCertifications(Collections.singletonList(cert));

        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER012", ex.getErrorCode());
    }

    @Test
    @DisplayName("Điểm chứng chỉ âm -> ném ER018")
    void testValidateCert_NegativeScore_ThrowsER018() {
        when(employeeRepository.existsByEmployeeLoginId("test_user")).thenReturn(false);
        when(departmentRepository.existsById(1L)).thenReturn(true);
        when(certificationRepository.existsById(1L)).thenReturn(true);

        EmployeeCertificationRequest cert = new EmployeeCertificationRequest();
        cert.setCertificationId(1L);
        cert.setCertificationStartDate("2023/01/01");
        cert.setCertificationEndDate("2024/01/01");
        cert.setEmployeeCertificationScore(new BigDecimal("-10"));
        validRequest.setCertifications(Collections.singletonList(cert));

        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateAddRequest(validRequest));
        assertEquals("ER018", ex.getErrorCode());
        assertEquals("点数", ex.getParams().get(0));
    }

    @Test
    @DisplayName("validateEmployeeTelephone - chứa alphabet halfsize (0901234abc) -> hợp lệ")
    void testValidateTelephone_HalfsizeAlphabet_Success() {
        assertDoesNotThrow(() -> employeeValidator.validateEmployeeTelephone("0901234abc"));
    }

    @Test
    @DisplayName("validateEmployeeTelephone - chứa full-width zenkaku -> ném ER008")
    void testValidateTelephone_FullwidthZenkaku_ThrowsER008() {
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateEmployeeTelephone("０９０１２３４ａｂｃ"));
        assertEquals("ER008", ex.getErrorCode());
        assertEquals("電話番号", ex.getParams().get(0));
    }

    @Test
    @DisplayName("validateUpdateRequest - employeeId không tồn tại -> ném ER013")
    void testValidateUpdateRequest_EmployeeNotFound_ThrowsER013() {
        UpdateEmployeeRequest updateReq = new UpdateEmployeeRequest();
        updateReq.setEmployeeId(999L);
        updateReq.setEmployeeLoginId("test_user");

        when(employeeRepository.findByEmployeeId(999L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateUpdateRequest(updateReq));
        assertEquals("ER013", ex.getErrorCode());
    }

    @Test
    @DisplayName("validateUpdateRequest - employeeLoginId không khớp trong DB -> ném ER003")
    void testValidateUpdateRequest_LoginIdMismatch_ThrowsER003() {
        UpdateEmployeeRequest updateReq = new UpdateEmployeeRequest();
        updateReq.setEmployeeId(1L);
        updateReq.setEmployeeLoginId("hacker_user");

        EmployeeEntity dbEntity = new EmployeeEntity();
        dbEntity.setEmployeeId(1L);
        dbEntity.setEmployeeLoginId("original_user");

        when(employeeRepository.findByEmployeeId(1L)).thenReturn(Optional.of(dbEntity));

        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateUpdateRequest(updateReq));
        assertEquals("ER003", ex.getErrorCode());
        assertEquals("アカウント名", ex.getParams().get(0));
    }

    @Test
    @DisplayName("validateUpdateRequest - hợp lệ không đổi mật khẩu -> thành công")
    void testValidateUpdateRequest_ValidWithoutPassword_Success() {
        UpdateEmployeeRequest updateReq = new UpdateEmployeeRequest();
        updateReq.setEmployeeId(1L);
        updateReq.setEmployeeLoginId("test_user");
        updateReq.setEmployeeName("Nguyễn Văn A");
        updateReq.setEmployeeNameKana("ｸﾞｴﾝ ｳﾞｧﾝ ｴｰ");
        updateReq.setEmployeeBirthDate("1995/05/15");
        updateReq.setEmployeeEmail("test@luvina.net");
        updateReq.setEmployeeTelephone("0123456789");
        updateReq.setEmployeeLoginPassword(""); // Mật khẩu để trống
        updateReq.setDepartmentId(1L);

        EmployeeEntity dbEntity = new EmployeeEntity();
        dbEntity.setEmployeeId(1L);
        dbEntity.setEmployeeLoginId("test_user");

        when(employeeRepository.findByEmployeeId(1L)).thenReturn(Optional.of(dbEntity));
        when(departmentRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> employeeValidator.validateUpdateRequest(updateReq));
    }

    @Test
    @DisplayName("validateDeleteRequest - ID hợp lệ và role User (0) -> thành công trả về entity")
    void testValidateDeleteRequest_SuccessUser() {
        EmployeeEntity userEntity = new EmployeeEntity();
        userEntity.setEmployeeId(10L);
        userEntity.setRole(0);

        when(employeeRepository.findByEmployeeId(10L)).thenReturn(Optional.of(userEntity));

        EmployeeEntity result = employeeValidator.validateDeleteRequest("10");
        assertNotNull(result);
        assertEquals(10L, result.getEmployeeId());
    }

    @Test
    @DisplayName("validateDeleteRequest - role Admin (1) -> ném ngoại lệ ER020")
    void testValidateDeleteRequest_AdminUser_ThrowsER020() {
        EmployeeEntity adminEntity = new EmployeeEntity();
        adminEntity.setEmployeeId(1L);
        adminEntity.setRole(1);

        when(employeeRepository.findByEmployeeId(1L)).thenReturn(Optional.of(adminEntity));

        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateDeleteRequest("1"));
        assertEquals("ER020", ex.getErrorCode());
    }

    @Test
    @DisplayName("validateDeleteRequest - nhân viên không tồn tại -> ném ngoại lệ ER014")
    void testValidateDeleteRequest_NotFound_ThrowsER014() {
        when(employeeRepository.findByEmployeeId(999L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateDeleteRequest("999"));
        assertEquals("ER014", ex.getErrorCode());
    }

    @Test
    @DisplayName("validateDeleteRequest - ID không phải số nguyên dương -> ném ngoại lệ ER018")
    void testValidateDeleteRequest_InvalidId_ThrowsER018() {
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateDeleteRequest("-5"));
        assertEquals("ER018", ex.getErrorCode());

        AppException ex2 = assertThrows(AppException.class, () -> employeeValidator.validateDeleteRequest("abc"));
        assertEquals("ER018", ex2.getErrorCode());
    }

    @Test
    @DisplayName("validateDeleteRequest - ID rỗng -> ném ngoại lệ ER001")
    void testValidateDeleteRequest_EmptyId_ThrowsER001() {
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateDeleteRequest(""));
        assertEquals("ER001", ex.getErrorCode());
    }

    @Test
    @DisplayName("validateSortParam - giá trị khác ASC/DESC -> ném ngoại lệ ER021")
    void testValidateSortParam_Invalid_ThrowsER021() {
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateSortParam("INVALID", "ord_employee_name"));
        assertEquals("ER021", ex.getErrorCode());
    }

    @Test
    @DisplayName("validateSortParam - ASC / DESC / null -> không ném ngoại lệ")
    void testValidateSortParam_Valid_Success() {
        assertDoesNotThrow(() -> employeeValidator.validateSortParam("ASC", "ord_employee_name"));
        assertDoesNotThrow(() -> employeeValidator.validateSortParam("DESC", "ord_employee_name"));
        assertDoesNotThrow(() -> employeeValidator.validateSortParam(null, "ord_employee_name"));
    }

    @Test
    @DisplayName("parsePositiveIntParam - giá trị âm hoặc chữ -> ném ngoại lệ ER018")
    void testParsePositiveIntParam_Invalid_ThrowsER018() {
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.parsePositiveIntParam("-1", 0, "オフセット"));
        assertEquals("ER018", ex.getErrorCode());

        AppException ex2 = assertThrows(AppException.class, () -> employeeValidator.parsePositiveIntParam("xyz", 0, "オフセット"));
        assertEquals("ER018", ex2.getErrorCode());
    }

    @Test
    @DisplayName("validateSearchEmployeeName - vượt quá 125 ký tự -> ném ngoại lệ ER006")
    void testValidateSearchEmployeeName_TooLong_ThrowsER006() {
        AppException ex = assertThrows(AppException.class, () -> employeeValidator.validateSearchEmployeeName("a".repeat(126)));
        assertEquals("ER006", ex.getErrorCode());
    }
}

