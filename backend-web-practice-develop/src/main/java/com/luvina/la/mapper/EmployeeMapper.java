package com.luvina.la.mapper;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeMapper.java, Aug 21, 2026 nvquy
 */

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper chuyển đổi giữa EmployeeEntity và EmployeeDTO.
 * Sử dụng MapStruct để tự động generate code mapping tại compile time.
 *
 * <p>Cách sử dụng:
 * <pre>
 *   EmployeeMapper.MAPPER.toEntity(dto);
 *   EmployeeMapper.MAPPER.toDto(entity);
 *   EmployeeMapper.MAPPER.toList(entityList);
 * </pre>
 *
 * @author quynv
 */
@Mapper
public interface EmployeeMapper {

    /** Singleton instance của mapper */
    EmployeeMapper MAPPER = Mappers.getMapper(EmployeeMapper.class);

    /**
     * Chuyển đổi từ EmployeeDTO sang EmployeeEntity.
     *
     * @param dto DTO chứa thông tin nhân viên cần chuyển đổi
     * @return EmployeeEntity tương ứng
     */
    EmployeeEntity toEntity(EmployeeDTO dto);

    /**
     * Chuyển đổi từ EmployeeEntity sang EmployeeDTO.
     *
     * @param entity Entity chứa thông tin nhân viên cần chuyển đổi
     * @return EmployeeDTO tương ứng
     */
    EmployeeDTO toDto(EmployeeEntity entity);

    /**
     * Chuyển đổi danh sách EmployeeEntity sang danh sách EmployeeDTO.
     *
     * @param list Danh sách entity cần chuyển đổi
     * @return Danh sách DTO tương ứng
     */
    Iterable<EmployeeDTO> toList(Iterable<EmployeeEntity> list);
}
