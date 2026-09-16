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
import com.luvina.la.payload.request.AddEmployeeRequest;
import com.luvina.la.payload.request.EmployeeCertificationRequest;
import com.luvina.la.payload.request.UpdateEmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeCertificationRepository;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private final DepartmentRepository departmentRepository;
    private final CertificationRepository certificationRepository;
    private final EmployeeCertificationRepository employeeCertificationRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               CertificationRepository certificationRepository,
                               EmployeeCertificationRepository employeeCertificationRepository,
                               PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.certificationRepository = certificationRepository;
        this.employeeCertificationRepository = employeeCertificationRepository;
        this.passwordEncoder = passwordEncoder;
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
                employeeName, departmentId,
                ordEmployeeName, ordCertificationName, ordEndDate, sortPriority,
                offset, limit);

        return new EmployeeSearchResultDTO(totalRecords, employees);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AddEmployeeDTO addEmployee(AddEmployeeRequest request) {
        DepartmentEntity departmentEntity = departmentRepository.findById(request.getDepartmentId()).orElse(null);

        Date employeeBirthDate = DateTimeUtil.parseDate(request.getEmployeeBirthDate().trim());

        EmployeeEntity newEmployeeEntity = new EmployeeEntity();
        mapEmployeeBasicInfo(newEmployeeEntity,
                request.getEmployeeName(),
                request.getEmployeeNameKana(),
                employeeBirthDate,
                request.getEmployeeEmail(),
                request.getEmployeeTelephone(),
                departmentEntity);
        newEmployeeEntity.setEmployeeLoginId(request.getEmployeeLoginId().trim());
        newEmployeeEntity.setEmployeeLoginPassword(passwordEncoder.encode(request.getEmployeeLoginPassword().trim()));
        newEmployeeEntity.setRole(Constants.ROLE_USER);

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

        List<EmployeeDetailDTO.EmployeeCertificationDetailDTO> certificationDetailDTOs = new ArrayList<>();
        for (EmployeeCertificationEntity employeeCertificationEntity : employeeCertificationEntities) {
            EmployeeDetailDTO.EmployeeCertificationDetailDTO certificationDetailDTO = new EmployeeDetailDTO.EmployeeCertificationDetailDTO();
            if (employeeCertificationEntity.getCertification() != null) {
                certificationDetailDTO.setCertificationId(employeeCertificationEntity.getCertification().getCertificationId());
                certificationDetailDTO.setCertificationName(employeeCertificationEntity.getCertification().getCertificationName());
            }
            certificationDetailDTO.setStartDate(DateTimeUtil.formatDate(employeeCertificationEntity.getStartDate()));
            certificationDetailDTO.setEndDate(DateTimeUtil.formatDate(employeeCertificationEntity.getEndDate()));
            certificationDetailDTO.setScore(employeeCertificationEntity.getScore());
            certificationDetailDTOs.add(certificationDetailDTO);
        }

        EmployeeDetailDTO employeeDetailDTO = new EmployeeDetailDTO();
        employeeDetailDTO.setEmployeeId(employeeEntity.getEmployeeId());
        employeeDetailDTO.setEmployeeName(employeeEntity.getEmployeeName());
        employeeDetailDTO.setEmployeeBirthDate(DateTimeUtil.formatDate(employeeEntity.getEmployeeBirthDate()));
        if (employeeEntity.getDepartment() != null) {
            employeeDetailDTO.setDepartmentId(employeeEntity.getDepartment().getDepartmentId());
            employeeDetailDTO.setDepartmentName(employeeEntity.getDepartment().getDepartmentName());
        }
        employeeDetailDTO.setEmployeeEmail(employeeEntity.getEmployeeEmail());
        employeeDetailDTO.setEmployeeTelephone(employeeEntity.getEmployeeTelephone());
        employeeDetailDTO.setEmployeeNameKana(employeeEntity.getEmployeeNameKana());
        employeeDetailDTO.setEmployeeLoginId(employeeEntity.getEmployeeLoginId());
        employeeDetailDTO.setCertifications(certificationDetailDTOs);
        employeeDetailDTO.setRole(employeeEntity.getRole());

        return employeeDetailDTO;
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

        DepartmentEntity departmentEntity = departmentRepository.findById(request.getDepartmentId()).orElse(null);

        Date employeeBirthDate = DateTimeUtil.parseDate(request.getEmployeeBirthDate().trim());

        // Cập nhật thông tin cơ bản của Employee
        mapEmployeeBasicInfo(existingEmployeeEntity,
                request.getEmployeeName(),
                request.getEmployeeNameKana(),
                employeeBirthDate,
                request.getEmployeeEmail(),
                request.getEmployeeTelephone(),
                departmentEntity);

        // Mật khẩu: Chỉ cập nhật nếu người dùng nhập mật khẩu mới
        if (request.getEmployeeLoginPassword() != null && !request.getEmployeeLoginPassword().trim().isEmpty()) {
            existingEmployeeEntity.setEmployeeLoginPassword(passwordEncoder.encode(request.getEmployeeLoginPassword().trim()));
        }

        EmployeeEntity savedEmployeeEntity = employeeRepository.save(existingEmployeeEntity);

        // Xóa triệt để các chứng chỉ cũ trong DB và flush ngay lập tức
        employeeCertificationRepository.deleteByEmployee_EmployeeId(savedEmployeeEntity.getEmployeeId());
        employeeCertificationRepository.flush();

        // Thêm các chứng chỉ mới (nếu có)
        saveCertifications(savedEmployeeEntity, request.getCertifications());

        return new UpdateEmployeeDTO(savedEmployeeEntity.getEmployeeId());
    }

    /**
     * Gán các thông tin cơ bản của nhân viên vào Entity dùng chung cho cả thêm mới và cập nhật.
     */
    private void mapEmployeeBasicInfo(EmployeeEntity entity,
                                      String name,
                                      String nameKana,
                                      Date birthDate,
                                      String email,
                                      String telephone,
                                      DepartmentEntity department) {
        entity.setDepartment(department);
        entity.setEmployeeName(name.trim());
        entity.setEmployeeNameKana(nameKana.trim());
        entity.setEmployeeBirthDate(birthDate);
        entity.setEmployeeEmail(email.trim());
        entity.setEmployeeTelephone(telephone.trim());
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

            Date startDate = DateTimeUtil.parseDate(certReq.getCertificationStartDate().trim());
            Date endDate = DateTimeUtil.parseDate(certReq.getCertificationEndDate().trim());

            EmployeeCertificationEntity entity = new EmployeeCertificationEntity();
            entity.setEmployee(employee);
            entity.setCertification(certEntity);
            entity.setStartDate(startDate);
            entity.setEndDate(endDate);
            entity.setScore(certReq.getEmployeeCertificationScore().intValue());

            employeeCertificationRepository.save(entity);
        }
    }
}

