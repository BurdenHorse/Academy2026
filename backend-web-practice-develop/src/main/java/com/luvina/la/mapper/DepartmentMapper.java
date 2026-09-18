package com.luvina.la.mapper;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentMapper.java, Sep 17, 2026
 */

import com.luvina.la.config.Constants;
import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.entity.DepartmentEntity;
import com.luvina.la.payload.response.ListDepartmentResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Component;

/**
 * Mapper chuyển đổi giữa DepartmentEntity và DepartmentDTO.
 *
 * @author quynv
 */
@Component
public class DepartmentMapper {

    /**
     * Chuyển đổi từ DepartmentEntity sang DepartmentDTO.
     *
     * @param entity DepartmentEntity cần chuyển đổi
     * @return DepartmentDTO tương ứng
     */
    public DepartmentDTO toDTO(DepartmentEntity entity) {
        if (entity == null) {
            return null;
        }
        return new DepartmentDTO(
                String.valueOf(entity.getDepartmentId()),
                entity.getDepartmentName()
        );
    }

    /**
     * Chuyển đổi danh sách DepartmentEntity sang danh sách DepartmentDTO.
     *
     * @param entities Iterable các DepartmentEntity
     * @return Danh sách DepartmentDTO
     */
    public List<DepartmentDTO> toDTOList(Iterable<DepartmentEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Đóng gói danh sách DepartmentDTO sang ListDepartmentResponse cho Controller.
     *
     * @param departments Danh sách DepartmentDTO
     * @return ListDepartmentResponse với mã code 200
     */
    public ListDepartmentResponse toListResponse(List<DepartmentDTO> departments) {
        ListDepartmentResponse response = new ListDepartmentResponse();
        response.setCode(Constants.STATUS_CODE_SUCCESS);
        response.setDepartments(departments != null ? departments : Collections.emptyList());
        return response;
    }
}
