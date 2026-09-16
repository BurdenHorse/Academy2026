package com.luvina.la.service;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceTest.java, Sep 07, 2026
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.luvina.la.dto.AddEmployeeDTO;
import com.luvina.la.dto.DeleteEmployeeDTO;
import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.UpdateEmployeeDTO;
import com.luvina.la.entity.CertificationEntity;
import com.luvina.la.entity.DepartmentEntity;
import com.luvina.la.entity.EmployeeCertificationEntity;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.payload.request.AddEmployeeRequest;
import com.luvina.la.payload.request.EmployeeCertificationRequest;
import com.luvina.la.payload.request.UpdateEmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeCertificationRepository;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.impl.EmployeeServiceImpl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CertificationRepository certificationRepository;

    @Mock
    private EmployeeCertificationRepository employeeCertificationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private AddEmployeeRequest request;
    private DepartmentEntity department;
    private EmployeeEntity savedEmployee;

    @BeforeEach
    void setUp() {
        request = new AddEmployeeRequest();
        request.setEmployeeLoginId("new_emp");
        request.setEmployeeName("Trần Văn B");
        request.setEmployeeNameKana("チャン ヴァン ビー");
        request.setEmployeeBirthDate("1998/12/20");
        request.setEmployeeEmail("emp_b@luvina.net");
        request.setEmployeeTelephone("0987654321");
        request.setEmployeeLoginPassword("secretPass123");
        request.setDepartmentId(1L);

        department = new DepartmentEntity();
        department.setDepartmentId(1L);
        department.setDepartmentName("DEV1");

        savedEmployee = new EmployeeEntity();
        savedEmployee.setEmployeeId(100L);
        savedEmployee.setEmployeeLoginId("new_emp");
        savedEmployee.setRole(0);
    }

    @Test
    @DisplayName("addEmployee lưu thành công nhân viên không có chứng chỉ")
    void testAddEmployee_SuccessWithoutCertifications() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(passwordEncoder.encode("secretPass123")).thenReturn("encodedPassword");
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(savedEmployee);

        AddEmployeeDTO response = employeeService.addEmployee(request);

        assertNotNull(response);
        assertEquals(100L, response.getEmployeeId());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
        verify(employeeCertificationRepository, times(0)).save(any(EmployeeCertificationEntity.class));
    }

    @Test
    @DisplayName("addEmployee lưu thành công nhân viên có chứng chỉ tiếng Nhật")
    void testAddEmployee_SuccessWithCertifications() {
        EmployeeCertificationRequest certDTO = new EmployeeCertificationRequest();
        certDTO.setCertificationId(3L);
        certDTO.setCertificationStartDate("2022/05/10");
        certDTO.setCertificationEndDate("2024/05/10");
        certDTO.setEmployeeCertificationScore(new BigDecimal("120"));
        request.setCertifications(Collections.singletonList(certDTO));

        CertificationEntity certification = new CertificationEntity();
        certification.setCertificationId(3L);
        certification.setCertificationName("N3");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(passwordEncoder.encode("secretPass123")).thenReturn("encodedPassword");
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(savedEmployee);

        when(certificationRepository.findById(3L)).thenReturn(Optional.of(certification));

        AddEmployeeDTO response = employeeService.addEmployee(request);

        assertNotNull(response);
        assertEquals(100L, response.getEmployeeId());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
        verify(employeeCertificationRepository, times(1)).save(any(EmployeeCertificationEntity.class));
    }

    @Test
    @DisplayName("getEmployeeDetail thành công khi nhân viên tồn tại và có chứng chỉ")
    void testGetEmployeeDetail_Success() {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setEmployeeId(100L);
        emp.setEmployeeName("Trần Văn B");
        emp.setRole(0);
        emp.setDepartment(department);

        EmployeeCertificationEntity empCert = new EmployeeCertificationEntity();
        empCert.setEmployee(emp);
        CertificationEntity cert = new CertificationEntity();
        cert.setCertificationId(1L);
        cert.setCertificationName("N1");
        cert.setCertificationLevel(1);
        empCert.setCertification(cert);
        empCert.setStartDate(new Date(1000000000000L));
        empCert.setEndDate(new Date(1200000000000L));
        empCert.setScore(150);

        when(employeeRepository.findByEmployeeId(100L)).thenReturn(Optional.of(emp));
        when(employeeCertificationRepository.findByEmployee_EmployeeId(100L))
                .thenReturn(new java.util.ArrayList<>(Collections.singletonList(empCert)));

        EmployeeDetailDTO response = employeeService.getEmployeeDetail(100L);

        assertNotNull(response);
        assertEquals(100L, response.getEmployeeId());
        assertEquals("Trần Văn B", response.getEmployeeName());
        assertEquals("DEV1", response.getDepartmentName());
        assertEquals(0, response.getRole());
        assertEquals(1, response.getCertifications().size());
        assertEquals("N1", response.getCertifications().get(0).getCertificationName());
        assertEquals(150, response.getCertifications().get(0).getScore());
    }

    @Test
    @DisplayName("getEmployeeDetail ném ngoại lệ ER013 khi không tìm thấy nhân viên")
    void testGetEmployeeDetail_NotFound() {
        when(employeeRepository.findByEmployeeId(999L)).thenReturn(Optional.empty());

        com.luvina.la.exception.AppException ex = org.junit.jupiter.api.Assertions.assertThrows(
                com.luvina.la.exception.AppException.class,
                () -> employeeService.getEmployeeDetail(999L)
        );

        assertEquals("ER013", ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteEmployee xóa thành công khi nhân viên tồn tại")
    void testDeleteEmployee_Success() {
        DeleteEmployeeDTO response = employeeService.deleteEmployee(100L);

        assertNotNull(response);
        assertEquals(100L, response.getEmployeeId());

        verify(employeeCertificationRepository, times(1)).deleteByEmployee_EmployeeId(100L);
        verify(employeeRepository, times(1)).deleteById(100L);
    }

    @Test
    @DisplayName("updateEmployee cập nhật thành công thông tin nhân viên")
    void testUpdateEmployee_Success() {
        UpdateEmployeeRequest updateReq = new UpdateEmployeeRequest();
        updateReq.setEmployeeId(100L);
        updateReq.setEmployeeLoginId("new_emp");
        updateReq.setEmployeeName("Trần Văn Updated");
        updateReq.setEmployeeNameKana("チャン ヴァン アップデート");
        updateReq.setEmployeeBirthDate("1998/12/20");
        updateReq.setEmployeeEmail("updated@luvina.net");
        updateReq.setEmployeeTelephone("0987654321");
        updateReq.setEmployeeLoginPassword("newSecretPass456");
        updateReq.setDepartmentId(1L);

        when(employeeRepository.findByEmployeeId(100L)).thenReturn(Optional.of(savedEmployee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(passwordEncoder.encode("newSecretPass456")).thenReturn("hashed_new_pass");
        when(employeeRepository.save(any(EmployeeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateEmployeeDTO response = employeeService.updateEmployee(updateReq);

        assertNotNull(response);
        assertEquals(100L, response.getEmployeeId());
        verify(employeeCertificationRepository, times(1)).deleteByEmployee_EmployeeId(100L);
        verify(employeeRepository, times(1)).save(savedEmployee);
    }

    @Test
    @DisplayName("updateEmployee dọn sạch chứng chỉ cũ và thay thế bằng chứng chỉ mới")
    void testUpdateEmployee_ReplacesCertifications() {
        EmployeeCertificationEntity oldCert = new EmployeeCertificationEntity();
        oldCert.setEmployeeCertificationId(1L);
        oldCert.setEmployee(savedEmployee);
        java.util.List<EmployeeCertificationEntity> certList = new java.util.ArrayList<>();
        certList.add(oldCert);
        savedEmployee.setEmployeeCertifications(certList);

        UpdateEmployeeRequest updateReq = new UpdateEmployeeRequest();
        updateReq.setEmployeeId(100L);
        updateReq.setEmployeeLoginId("new_emp");
        updateReq.setEmployeeName("Trần Văn Updated");
        updateReq.setEmployeeNameKana("チャン ヴァン アップデート");
        updateReq.setEmployeeBirthDate("1998/12/20");
        updateReq.setEmployeeEmail("updated@luvina.net");
        updateReq.setEmployeeTelephone("0987654321");
        updateReq.setDepartmentId(1L);

        EmployeeCertificationRequest newCertReq = new EmployeeCertificationRequest();
        newCertReq.setCertificationId(1L);
        newCertReq.setCertificationStartDate("2024/01/01");
        newCertReq.setCertificationEndDate("2025/01/01");
        newCertReq.setEmployeeCertificationScore(new BigDecimal("150"));
        updateReq.setCertifications(Collections.singletonList(newCertReq));

        CertificationEntity certEntity = new CertificationEntity();
        certEntity.setCertificationId(1L);

        when(employeeRepository.findByEmployeeId(100L)).thenReturn(Optional.of(savedEmployee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(certificationRepository.findById(1L)).thenReturn(Optional.of(certEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateEmployeeDTO response = employeeService.updateEmployee(updateReq);

        assertNotNull(response);
        assertEquals(100L, response.getEmployeeId());
        verify(employeeCertificationRepository, times(1)).deleteByEmployee_EmployeeId(100L);
        verify(employeeCertificationRepository, times(1)).flush();
        verify(employeeCertificationRepository, times(1)).save(any(EmployeeCertificationEntity.class));
    }
}
