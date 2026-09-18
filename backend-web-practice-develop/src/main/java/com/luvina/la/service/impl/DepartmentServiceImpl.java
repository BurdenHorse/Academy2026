package com.luvina.la.service.impl;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentServiceImpl.java, Aug 22, 2026 nvquy
 */

import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.mapper.DepartmentMapper;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.service.DepartmentService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lớp triển khai DepartmentService, xử lý nghiệp vụ liên quan đến phòng ban.
 * Lấy dữ liệu từ DepartmentRepository và chuyển đổi sang DepartmentDTO.
 *
 * @author quynv
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    /**
     * Constructor injection cho DepartmentRepository và DepartmentMapper.
     *
     * @param departmentRepository Repository truy vấn dữ liệu phòng ban
     * @param departmentMapper     Mapper chuyển đổi Entity sang DTO
     */
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * {@inheritDoc}
     * Lấy toàn bộ phòng ban từ database, chuyển đổi sang DepartmentDTO
     * với departmentId dạng String theo specification API.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentMapper.toDTOList(departmentRepository.findAllByOrderByDepartmentIdAsc());
    }
}
