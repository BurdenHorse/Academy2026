package com.luvina.la.repository;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeRepository.java, Aug 17, 2026 nvquy
 */

import java.util.Optional;
import com.luvina.la.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository truy vấn dữ liệu nhân viên.
 * Kế thừa JpaRepository cho các thao tác CRUD cơ bản (findById, save, delete,...)
 * và EmployeeRepositoryCustom cho các truy vấn phức tạp
 * (tìm kiếm theo tên/phòng ban, sắp xếp đa cột, phân trang offset/limit).
 *
 * @author quynv
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>, EmployeeRepositoryCustom {

    /**
     * Tìm nhân viên theo login ID.
     * Dùng cho chức năng đăng nhập (authentication).
     *
     * @param employeeLoginId Login ID cần tìm
     * @return Optional chứa entity nhân viên nếu tìm thấy, empty nếu không
     */
    Optional<EmployeeEntity> findByEmployeeLoginId(String employeeLoginId);

    /**
     * Tìm nhân viên theo employee ID.
     * Dùng cho chức năng xem chi tiết nhân viên (ADM003).
     *
     * @param employeeId ID nhân viên cần tìm
     * @return Optional chứa entity nhân viên nếu tìm thấy, empty nếu không
     */
    Optional<EmployeeEntity> findByEmployeeId(Long employeeId);

    /**
     * Kiểm tra sự tồn tại của employeeLoginId trong database.
     *
     * @param employeeLoginId Tên đăng nhập cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByEmployeeLoginId(String employeeLoginId);

    /**
     * Kiểm tra sự tồn tại của employeeEmail trong database.
     *
     * @param employeeEmail Email cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByEmployeeEmail(String employeeEmail);
}
