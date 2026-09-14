package com.luvina.la.repository;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeCertificationRepository.java, Aug 26, 2026 nvquy
 */

import com.luvina.la.entity.EmployeeCertificationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository truy vấn và lưu trữ dữ liệu bảng employees_certifications.
 *
 * @author quynv
 */
@Repository
public interface EmployeeCertificationRepository extends JpaRepository<EmployeeCertificationEntity, Long> {

    /**
     * Tìm danh sách chứng chỉ theo employee_id.
     *
     * @param employeeId ID nhân viên
     * @return Danh sách chứng chỉ của nhân viên
     */
    List<EmployeeCertificationEntity> findByEmployee_EmployeeId(Long employeeId);

    /**
     * Xóa toàn bộ chứng chỉ của nhân viên theo employee_id.
     *
     * @param employeeId ID nhân viên
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM EmployeeCertificationEntity ec WHERE ec.employee.employeeId = :employeeId")
    void deleteByEmployee_EmployeeId(@Param("employeeId") Long employeeId);
}
