package com.luvina.la.service.impl;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeServiceImpl.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.config.Constants;
import com.luvina.la.dto.AddEmployeeDTO;
import com.luvina.la.dto.DeleteEmployeeDTO;
import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.dto.EmployeeSearchResultDTO;
import com.luvina.la.dto.UpdateEmployeeDTO;
import com.luvina.la.entity.CertificationEntity;
import com.luvina.la.entity.DepartmentEntity;
import com.luvina.la.entity.EmployeeCertificationEntity;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.exception.AppException;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.request.AddEmployeeRequest;
import com.luvina.la.payload.request.EmployeeCertificationRequest;
import com.luvina.la.payload.request.UpdateEmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.EmployeeCertificationRepository;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lớp triển khai EmployeeService.
 * Xử lý nghiệp vụ tìm kiếm, phân trang, thêm mới, xem chi tiết và xóa nhân viên.
 * Trả về các đối tượng DTO thuần túy cho Controller.
 *
 * @author quynv
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CertificationRepository certificationRepository;
    private final EmployeeCertificationRepository employeeCertificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               CertificationRepository certificationRepository,
                               EmployeeCertificationRepository employeeCertificationRepository,
                               PasswordEncoder passwordEncoder,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.certificationRepository = certificationRepository;
        this.employeeCertificationRepository = employeeCertificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeMapper = employeeMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeSearchResultDTO getEmployeeList(String employeeName, Long departmentId,
                                                  String ordEmployeeName, String ordCertificationName,
                                                  String ordEndDate, String sortPriority,
                                                  int offset, int limit) {
        long totalRecords = employeeRepository.countEmployees(employeeName, departmentId);

        if (totalRecords == 0) {
            return new EmployeeSearchResultDTO(0L, Collections.emptyList());
        }

        List<EmployeeListDTO> employees = employeeRepository.searchEmployees(
                employeeName,
                departmentId,
                ordEmployeeName,
                ordCertificationName,
                ordEndDate,
                sortPriority,
                offset,
                limit);

        return new EmployeeSearchResultDTO(totalRecords, employees);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AddEmployeeDTO addEmployee(AddEmployeeRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getEmployeeLoginPassword().trim());
        EmployeeEntity newEmployeeEntity = employeeMapper.toEntity(request, encodedPassword);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(newEmployeeEntity);
        saveCertifications(savedEmployeeEntity, request.getCertifications());

        return new AddEmployeeDTO(savedEmployeeEntity.getEmployeeId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeDetailDTO getEmployeeDetail(Long employeeId) {
        EmployeeEntity employeeEntity = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new AppException(Constants.ERROR_CODE_USER_NOT_FOUND_GET, Collections.emptyList()));

        // Lấy danh sách chứng chỉ và sắp xếp theo certification_level ASC
        List<EmployeeCertificationEntity> employeeCertificationEntities = employeeCertificationRepository.findByEmployee_EmployeeId(employeeId);
        employeeCertificationEntities.sort(Comparator.comparing(
                ec -> ec.getCertification() != null && ec.getCertification().getCertificationLevel() != null
                        ? ec.getCertification().getCertificationLevel()
                        : Integer.MAX_VALUE
        ));

        return employeeMapper.toDetailDTO(employeeEntity, employeeCertificationEntities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DeleteEmployeeDTO deleteEmployee(Long employeeId) {
        employeeCertificationRepository.deleteByEmployee_EmployeeId(employeeId);
        employeeRepository.deleteById(employeeId);
        return new DeleteEmployeeDTO(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UpdateEmployeeDTO updateEmployee(UpdateEmployeeRequest request) {
        EmployeeEntity existingEmployeeEntity = employeeRepository.findByEmployeeId(request.getEmployeeId()).orElse(null);

        // Cập nhật thông tin cơ bản của Employee qua Mapper
        employeeMapper.updateEntity(existingEmployeeEntity, request);

        // Mật khẩu: Chỉ cập nhật nếu người dùng nhập mật khẩu mới
        if (request.getEmployeeLoginPassword() != null && !request.getEmployeeLoginPassword().trim().isEmpty()) {
            existingEmployeeEntity.setEmployeeLoginPassword(passwordEncoder.encode(request.getEmployeeLoginPassword().trim()));
        }

        EmployeeEntity savedEmployeeEntity = employeeRepository.save(existingEmployeeEntity);

        // Xóa các chứng chỉ cũ trong DB
        employeeCertificationRepository.deleteByEmployee_EmployeeId(savedEmployeeEntity.getEmployeeId());
        employeeCertificationRepository.flush();

        // Thêm các chứng chỉ mới (nếu có)
        saveCertifications(savedEmployeeEntity, request.getCertifications());

        return new UpdateEmployeeDTO(savedEmployeeEntity.getEmployeeId());
    }

    /**
     * Lưu danh sách chứng chỉ tiếng Nhật của nhân viên dùng chung cho cả thêm mới và cập nhật.
     */
    private void saveCertifications(EmployeeEntity employee, List<EmployeeCertificationRequest> certificationRequests) {
        if (certificationRequests == null || certificationRequests.isEmpty()) {
            return;
        }
        for (EmployeeCertificationRequest certReq : certificationRequests) {
            CertificationEntity certEntity = certificationRepository.findById(certReq.getCertificationId()).orElse(null);
            EmployeeCertificationEntity entity = employeeMapper.toCertificationEntity(certReq, employee, certEntity);
            employeeCertificationRepository.save(entity);
        }
    }
}

