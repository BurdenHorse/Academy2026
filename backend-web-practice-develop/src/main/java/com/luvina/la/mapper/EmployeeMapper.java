package com.luvina.la.mapper;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeMapper.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.config.Constants;
import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.dto.EmployeeDetailDTO;
import com.luvina.la.dto.EmployeeDetailDTO.EmployeeCertificationDetailDTO;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.dto.EmployeeSearchResultDTO;
import com.luvina.la.entity.CertificationEntity;
import com.luvina.la.entity.EmployeeCertificationEntity;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.payload.request.AddEmployeeRequest;
import com.luvina.la.payload.request.EmployeeCertificationRequest;
import com.luvina.la.payload.request.UpdateEmployeeRequest;
import com.luvina.la.payload.response.EmployeeDetailResponse;
import com.luvina.la.payload.response.ListEmployeeResponse;
import com.luvina.la.util.DateTimeUtil;
import com.luvina.la.util.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Mapper chuyển đổi dữ liệu nhân viên giữa Entity, DTO và Response Payload.
 *
 * @author quynv
 */
@Component
public class EmployeeMapper {

    /**
     * Chuyển đổi EmployeeEntity và danh sách EmployeeCertificationEntity sang EmployeeDetailDTO (ADM003).
     *
     * @param employeeEntity                Entity nhân viên
     * @param employeeCertificationEntities Danh sách entity chứng chỉ của nhân viên
     * @return EmployeeDetailDTO chứa thông tin chi tiết đầy đủ
     */
    public EmployeeDetailDTO toDetailDTO(EmployeeEntity employeeEntity,
                                         List<EmployeeCertificationEntity> employeeCertificationEntities) {
        if (employeeEntity == null) {
            return null;
        }

        List<EmployeeCertificationDetailDTO> certDetailDTOs = toCertificationDetailDTOList(employeeCertificationEntities);

        EmployeeDetailDTO dto = new EmployeeDetailDTO();
        dto.setEmployeeId(employeeEntity.getEmployeeId());
        dto.setEmployeeName(employeeEntity.getEmployeeName());
        dto.setEmployeeBirthDate(DateTimeUtil.formatDate(employeeEntity.getEmployeeBirthDate()));

        if (employeeEntity.getDepartment() != null) {
            dto.setDepartmentId(employeeEntity.getDepartment().getDepartmentId());
            dto.setDepartmentName(employeeEntity.getDepartment().getDepartmentName());
        } else if (employeeEntity.getDepartmentId() != null) {
            dto.setDepartmentId(employeeEntity.getDepartmentId());
        }

        dto.setEmployeeEmail(employeeEntity.getEmployeeEmail());
        dto.setEmployeeTelephone(employeeEntity.getEmployeeTelephone());
        dto.setEmployeeNameKana(employeeEntity.getEmployeeNameKana());
        dto.setEmployeeLoginId(employeeEntity.getEmployeeLoginId());
        dto.setCertifications(certDetailDTOs);
        dto.setRole(employeeEntity.getRole());

        return dto;
    }

    /**
     * Chuyển đổi danh sách EmployeeCertificationEntity sang danh sách EmployeeCertificationDetailDTO.
     *
     * @param certEntities Danh sách entity chứng chỉ
     * @return Danh sách DTO chứng chỉ
     */
    public List<EmployeeCertificationDetailDTO> toCertificationDetailDTOList(List<EmployeeCertificationEntity> certEntities) {
        if (certEntities == null || certEntities.isEmpty()) {
            return Collections.emptyList();
        }
        List<EmployeeCertificationDetailDTO> dtos = new ArrayList<>(certEntities.size());
        for (EmployeeCertificationEntity certEntity : certEntities) {
            dtos.add(toCertificationDetailDTO(certEntity));
        }
        return dtos;
    }

    /**
     * Chuyển đổi 1 bản ghi EmployeeCertificationEntity sang EmployeeCertificationDetailDTO.
     *
     * @param certEntity Entity chứng chỉ của nhân viên
     * @return EmployeeCertificationDetailDTO
     */
    public EmployeeCertificationDetailDTO toCertificationDetailDTO(EmployeeCertificationEntity certEntity) {
        if (certEntity == null) {
            return null;
        }
        EmployeeCertificationDetailDTO dto = new EmployeeCertificationDetailDTO();
        if (certEntity.getCertification() != null) {
            dto.setCertificationId(certEntity.getCertification().getCertificationId());
            dto.setCertificationName(certEntity.getCertification().getCertificationName());
        }
        dto.setStartDate(DateTimeUtil.formatDate(certEntity.getStartDate()));
        dto.setEndDate(DateTimeUtil.formatDate(certEntity.getEndDate()));
        dto.setScore(certEntity.getScore());
        return dto;
    }

    /**
     * Ánh xạ một dòng kết quả Native Query (Object[]) sang EmployeeListDTO.
     * Sử dụng biến chỉ số i++ tuần tự tương ứng với thứ tự các cột trong câu lệnh SELECT của Repository:
     * - 0: e.employee_id
     * - 1: e.employee_name
     * - 2: DATE_FORMAT(e.employee_birth_date, '%Y/%m/%d')
     * - 3: d.department_name
     * - 4: e.employee_email
     * - 5: e.employee_telephone
     * - 6: c.certification_name
     * - 7: DATE_FORMAT(ec.end_date, '%Y/%m/%d')
     * - 8: ec.score
     *
     * @param row Mảng Object[] đại diện cho 1 dòng dữ liệu trả về từ Native SQL
     * @return Đối tượng EmployeeListDTO
     */
    public EmployeeListDTO toEmployeeListDTO(Object[] row) {
        if (row == null) {
            return null;
        }
        int i = 0;
        Object employeeIdObj = row[i++];
        String employeeId = employeeIdObj != null ? String.valueOf(employeeIdObj) : null;
        String employeeName = (String) row[i++];
        String employeeBirthDate = (String) row[i++];
        String departmentName = (String) row[i++];
        String employeeEmail = (String) row[i++];
        String employeeTelephone = (String) row[i++];
        String certificationName = (String) row[i++];
        String endDate = (String) row[i++];
        Object scoreObj = row[i++];
        String score = scoreObj != null ? StringUtil.formatScore(scoreObj) : null;

        return new EmployeeListDTO(
                employeeId,
                employeeName,
                employeeBirthDate,
                departmentName,
                employeeEmail,
                employeeTelephone,
                certificationName,
                endDate,
                score
        );
    }

    /**
     * Ánh xạ danh sách kết quả Native Query (List<Object[]>) sang List<EmployeeListDTO>.
     *
     * @param rows Danh sách các mảng Object[] từ Native Query
     * @return Danh sách EmployeeListDTO
     */
    public List<EmployeeListDTO> toEmployeeListDTOList(List<Object[]> rows) {
        if (rows == null || rows.isEmpty()) {
            return Collections.emptyList();
        }
        List<EmployeeListDTO> dtos = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            dtos.add(toEmployeeListDTO(row));
        }
        return dtos;
    }

    /**
     * Đóng gói kết quả tìm kiếm nhân viên từ DTO sang ListEmployeeResponse cho Controller.
     *
     * @param resultDTO DTO kết quả tìm kiếm nhân viên
     * @return ListEmployeeResponse với mã code 200
     */
    public ListEmployeeResponse toListResponse(EmployeeSearchResultDTO resultDTO) {
        ListEmployeeResponse response = new ListEmployeeResponse();
        response.setCode(Constants.STATUS_CODE_SUCCESS);
        if (resultDTO != null) {
            response.setTotalRecords(resultDTO.getTotalRecords());
            response.setEmployees(resultDTO.getEmployees());
        } else {
            response.setTotalRecords(0L);
            response.setEmployees(Collections.emptyList());
        }
        return response;
    }

    /**
     * Đóng gói thông tin chi tiết nhân viên từ DTO sang EmployeeDetailResponse cho Controller.
     *
     * @param dto DTO chi tiết nhân viên
     * @return EmployeeDetailResponse
     */
    public EmployeeDetailResponse toDetailResponse(EmployeeDetailDTO dto) {
        if (dto == null) {
            return null;
        }

        List<EmployeeDetailResponse.EmployeeCertificationDetailDTO> certResponses = null;
        if (dto.getCertifications() != null) {
            certResponses = dto.getCertifications().stream()
                    .map(c -> new EmployeeDetailResponse.EmployeeCertificationDetailDTO(
                            c.getCertificationId(),
                            c.getCertificationName(),
                            c.getStartDate(),
                            c.getEndDate(),
                            c.getScore()))
                    .collect(Collectors.toList());
        }

        return new EmployeeDetailResponse(
                Constants.STATUS_CODE_SUCCESS,
                dto.getEmployeeId(),
                dto.getEmployeeName(),
                dto.getEmployeeBirthDate(),
                dto.getDepartmentId(),
                dto.getDepartmentName(),
                dto.getEmployeeEmail(),
                dto.getEmployeeTelephone(),
                dto.getEmployeeNameKana(),
                dto.getEmployeeLoginId(),
                certResponses,
                dto.getRole()
        );
    }

    /**
     * Chuyển đổi AddEmployeeRequest sang EmployeeEntity khi thêm mới nhân viên.
     *
     * @param request         Request thêm mới nhân viên
     * @param encodedPassword Mật khẩu đã được mã hóa BCrypt
     * @return EmployeeEntity mới
     */
    public EmployeeEntity toEntity(AddEmployeeRequest request, String encodedPassword) {
        if (request == null) {
            return null;
        }
        Date birthDate = DateTimeUtil.parseDate(request.getEmployeeBirthDate().trim());
        EmployeeEntity entity = new EmployeeEntity();
        entity.setDepartmentId(request.getDepartmentId());
        entity.setEmployeeName(request.getEmployeeName().trim());
        entity.setEmployeeNameKana(request.getEmployeeNameKana().trim());
        entity.setEmployeeBirthDate(birthDate);
        entity.setEmployeeEmail(request.getEmployeeEmail().trim());
        entity.setEmployeeTelephone(request.getEmployeeTelephone().trim());
        entity.setEmployeeLoginId(request.getEmployeeLoginId().trim());
        entity.setEmployeeLoginPassword(encodedPassword);
        entity.setRole(Constants.ROLE_USER);
        return entity;
    }

    /**
     * Cập nhật các thông tin cơ bản từ UpdateEmployeeRequest vào EmployeeEntity đã có.
     *
     * @param entity  EmployeeEntity cần cập nhật
     * @param request Request cập nhật nhân viên
     */
    public void updateEntity(EmployeeEntity entity, UpdateEmployeeRequest request) {
        if (entity == null || request == null) {
            return;
        }
        Date birthDate = DateTimeUtil.parseDate(request.getEmployeeBirthDate().trim());
        entity.setDepartmentId(request.getDepartmentId());
        entity.setEmployeeName(request.getEmployeeName().trim());
        entity.setEmployeeNameKana(request.getEmployeeNameKana().trim());
        entity.setEmployeeBirthDate(birthDate);
        entity.setEmployeeEmail(request.getEmployeeEmail().trim());
        entity.setEmployeeTelephone(request.getEmployeeTelephone().trim());
    }

    /**
     * Chuyển đổi EmployeeCertificationRequest sang EmployeeCertificationEntity.
     *
     * @param certReq    Request thông tin chứng chỉ
     * @param employee   Entity nhân viên sở hữu
     * @param certEntity Entity chứng chỉ từ DB
     * @return EmployeeCertificationEntity
     */
    public EmployeeCertificationEntity toCertificationEntity(EmployeeCertificationRequest certReq,
                                                              EmployeeEntity employee,
                                                              CertificationEntity certEntity) {
        if (certReq == null) {
            return null;
        }
        Date startDate = DateTimeUtil.parseDate(certReq.getCertificationStartDate().trim());
        Date endDate = DateTimeUtil.parseDate(certReq.getCertificationEndDate().trim());

        EmployeeCertificationEntity entity = new EmployeeCertificationEntity();
        entity.setEmployee(employee);
        entity.setCertification(certEntity);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        if (certReq.getEmployeeCertificationScore() != null) {
            entity.setScore(certReq.getEmployeeCertificationScore().intValue());
        }
        return entity;
    }
}
