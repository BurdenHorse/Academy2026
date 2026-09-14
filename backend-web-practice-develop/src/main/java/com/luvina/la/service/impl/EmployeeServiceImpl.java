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
import com.luvina.la.payload.request.EmployeeCertificationRequestDTO;
import com.luvina.la.payload.request.UpdateEmployeeRequest;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeCertificationRepository;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.EmployeeValidator;
import java.text.SimpleDateFormat;
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
    private final EmployeeValidator employeeValidator;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               CertificationRepository certificationRepository,
                               EmployeeCertificationRepository employeeCertificationRepository,
                               PasswordEncoder passwordEncoder,
                               EmployeeValidator employeeValidator) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.certificationRepository = certificationRepository;
        this.employeeCertificationRepository = employeeCertificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeValidator = employeeValidator;
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
        // Lấy thông tin phòng ban
        DepartmentEntity departmentEntity = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new AppException(Constants.ERROR_CODE_NOT_FOUND, Collections.singletonList(Constants.PARAM_NAME_GROUP)));

        Date employeeBirthDate = employeeValidator.parseDate(request.getEmployeeBirthDate().trim(), Constants.PARAM_NAME_BIRTH_DATE);

        // Tạo và lưu EmployeeEntity
        EmployeeEntity newEmployeeEntity = new EmployeeEntity();
        newEmployeeEntity.setDepartment(departmentEntity);
        newEmployeeEntity.setEmployeeName(request.getEmployeeName().trim());
        newEmployeeEntity.setEmployeeNameKana(request.getEmployeeNameKana().trim());
        newEmployeeEntity.setEmployeeBirthDate(employeeBirthDate);
        newEmployeeEntity.setEmployeeEmail(request.getEmployeeEmail().trim());
        newEmployeeEntity.setEmployeeTelephone(request.getEmployeeTelephone().trim());
        newEmployeeEntity.setEmployeeLoginId(request.getEmployeeLoginId().trim());
        newEmployeeEntity.setEmployeeLoginPassword(passwordEncoder.encode(request.getEmployeeLoginPassword().trim()));
        newEmployeeEntity.setRole(Constants.ROLE_USER);

        EmployeeEntity savedEmployeeEntity = employeeRepository.save(newEmployeeEntity);

        // Lưu certifications (nếu có)
        List<EmployeeCertificationRequestDTO> certificationRequests = request.getCertifications();
        if (certificationRequests != null && !certificationRequests.isEmpty()) {
            for (EmployeeCertificationRequestDTO certificationRequestDTO : certificationRequests) {
                CertificationEntity certificationEntity = certificationRepository.findById(certificationRequestDTO.getCertificationId())
                        .orElseThrow(() -> new AppException(Constants.ERROR_CODE_NOT_FOUND, Collections.singletonList(Constants.PARAM_NAME_CERTIFICATION)));

                Date certificationStartDate = employeeValidator.parseDate(certificationRequestDTO.getCertificationStartDate().trim(), Constants.PARAM_NAME_CERT_START_DATE);
                Date certificationEndDate = employeeValidator.parseDate(certificationRequestDTO.getCertificationEndDate().trim(), Constants.PARAM_NAME_CERT_END_DATE);

                EmployeeCertificationEntity employeeCertificationEntity = new EmployeeCertificationEntity();
                employeeCertificationEntity.setEmployee(savedEmployeeEntity);
                employeeCertificationEntity.setCertification(certificationEntity);
                employeeCertificationEntity.setStartDate(certificationStartDate);
                employeeCertificationEntity.setEndDate(certificationEndDate);
                employeeCertificationEntity.setScore(certificationRequestDTO.getEmployeeCertificationScore().intValue());

                employeeCertificationRepository.save(employeeCertificationEntity);
            }
        }

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

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

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
            certificationDetailDTO.setStartDate(employeeCertificationEntity.getStartDate() != null ? dateFormatter.format(employeeCertificationEntity.getStartDate()) : "");
            certificationDetailDTO.setEndDate(employeeCertificationEntity.getEndDate() != null ? dateFormatter.format(employeeCertificationEntity.getEndDate()) : "");
            certificationDetailDTO.setScore(employeeCertificationEntity.getScore());
            certificationDetailDTOs.add(certificationDetailDTO);
        }

        EmployeeDetailDTO employeeDetailDTO = new EmployeeDetailDTO();
        employeeDetailDTO.setEmployeeId(employeeEntity.getEmployeeId());
        employeeDetailDTO.setEmployeeName(employeeEntity.getEmployeeName());
        employeeDetailDTO.setEmployeeBirthDate(employeeEntity.getEmployeeBirthDate() != null ? dateFormatter.format(employeeEntity.getEmployeeBirthDate()) : "");
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
        EmployeeEntity employeeEntity = employeeRepository.findByEmployeeId(employeeId).orElse(null);
        if (employeeEntity != null) {
            employeeRepository.delete(employeeEntity);
        }
        return new DeleteEmployeeDTO(employeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UpdateEmployeeDTO updateEmployee(UpdateEmployeeRequest request) {
        EmployeeEntity existingEmployeeEntity = employeeRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new AppException(Constants.ERROR_CODE_USER_NOT_FOUND_GET, Collections.singletonList(Constants.PARAM_NAME_ID)));

        DepartmentEntity departmentEntity = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new AppException(Constants.ERROR_CODE_NOT_FOUND, Collections.singletonList(Constants.PARAM_NAME_GROUP)));

        Date employeeBirthDate = employeeValidator.parseDate(request.getEmployeeBirthDate().trim(), Constants.PARAM_NAME_BIRTH_DATE);

        // Cập nhật thông tin Employee
        existingEmployeeEntity.setDepartment(departmentEntity);
        existingEmployeeEntity.setEmployeeName(request.getEmployeeName().trim());
        existingEmployeeEntity.setEmployeeNameKana(request.getEmployeeNameKana().trim());
        existingEmployeeEntity.setEmployeeBirthDate(employeeBirthDate);
        existingEmployeeEntity.setEmployeeEmail(request.getEmployeeEmail().trim());
        existingEmployeeEntity.setEmployeeTelephone(request.getEmployeeTelephone().trim());

        // Mật khẩu: Chỉ cập nhật nếu người dùng nhập mật khẩu mới
        if (request.getEmployeeLoginPassword() != null && !request.getEmployeeLoginPassword().trim().isEmpty()) {
            existingEmployeeEntity.setEmployeeLoginPassword(passwordEncoder.encode(request.getEmployeeLoginPassword().trim()));
        }

        EmployeeEntity savedEmployeeEntity = employeeRepository.save(existingEmployeeEntity);

        // Xóa triệt để các chứng chỉ cũ trong DB và flush ngay lập tức
        employeeCertificationRepository.deleteByEmployee_EmployeeId(savedEmployeeEntity.getEmployeeId());
        employeeCertificationRepository.flush();

        // Thêm các chứng chỉ mới (nếu có)
        List<EmployeeCertificationRequestDTO> certificationRequests = request.getCertifications();
        if (certificationRequests != null && !certificationRequests.isEmpty()) {
            for (EmployeeCertificationRequestDTO certificationRequestDTO : certificationRequests) {
                CertificationEntity certificationEntity = certificationRepository.findById(certificationRequestDTO.getCertificationId())
                        .orElseThrow(() -> new AppException(Constants.ERROR_CODE_NOT_FOUND, Collections.singletonList(Constants.PARAM_NAME_CERTIFICATION)));

                Date certificationStartDate = employeeValidator.parseDate(certificationRequestDTO.getCertificationStartDate().trim(), Constants.PARAM_NAME_CERT_START_DATE);
                Date certificationEndDate = employeeValidator.parseDate(certificationRequestDTO.getCertificationEndDate().trim(), Constants.PARAM_NAME_CERT_END_DATE);

                EmployeeCertificationEntity employeeCertificationEntity = new EmployeeCertificationEntity();
                employeeCertificationEntity.setEmployee(savedEmployeeEntity);
                employeeCertificationEntity.setCertification(certificationEntity);
                employeeCertificationEntity.setStartDate(certificationStartDate);
                employeeCertificationEntity.setEndDate(certificationEndDate);
                employeeCertificationEntity.setScore(certificationRequestDTO.getEmployeeCertificationScore().intValue());

                employeeCertificationRepository.save(employeeCertificationEntity);
            }
        }

        return new UpdateEmployeeDTO(savedEmployeeEntity.getEmployeeId());
    }
}

