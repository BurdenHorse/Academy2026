package com.luvina.la.repository;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DepartmentRepository.java, Aug 17, 2026 nvquy
 */

import com.luvina.la.entity.DepartmentEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository truy vấn dữ liệu phòng ban.
 * Kế thừa CrudRepository cho các thao tác CRUD cơ bản (findAll, findById,...).
 * Dùng để lấy danh sách phòng ban cho dropdown tìm kiếm trên ADM002.
 *
 * @author quynv
 */
@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Long> {

    /**
     * Lấy danh sách tất cả phòng ban theo thứ tự ID tăng dần.
     *
     * @return Danh sách phòng ban sắp xếp theo departmentId ASC
     */
    List<DepartmentEntity> findAllByOrderByDepartmentIdAsc();
}
