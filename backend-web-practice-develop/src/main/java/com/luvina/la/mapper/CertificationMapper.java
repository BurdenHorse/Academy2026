package com.luvina.la.mapper;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationMapper.java, Sep 17, 2026
 */

import com.luvina.la.config.Constants;
import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.entity.CertificationEntity;
import com.luvina.la.payload.response.ListCertificationResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Mapper chuyển đổi giữa CertificationEntity và CertificationDTO.
 *
 * @author quynv
 */
@Component
public class CertificationMapper {

    /**
     * Chuyển đổi từ CertificationEntity sang CertificationDTO.
     *
     * @param entity CertificationEntity cần chuyển đổi
     * @return CertificationDTO tương ứng
     */
    public CertificationDTO toDTO(CertificationEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CertificationDTO(
                String.valueOf(entity.getCertificationId()),
                entity.getCertificationName(),
                entity.getCertificationLevel()
        );
    }

    /**
     * Chuyển đổi danh sách CertificationEntity sang danh sách CertificationDTO.
     *
     * @param entities Danh sách CertificationEntity
     * @return Danh sách CertificationDTO
     */
    public List<CertificationDTO> toDTOList(List<CertificationEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Đóng gói danh sách CertificationDTO sang ListCertificationResponse cho Controller.
     *
     * @param certifications Danh sách CertificationDTO
     * @return ListCertificationResponse với mã code 200
     */
    public ListCertificationResponse toListResponse(List<CertificationDTO> certifications) {
        ListCertificationResponse response = new ListCertificationResponse();
        response.setCode(Constants.STATUS_CODE_SUCCESS);
        response.setCertifications(certifications != null ? certifications : Collections.emptyList());
        return response;
    }
}
