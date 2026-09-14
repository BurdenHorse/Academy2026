package com.luvina.la.repository;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationRepository.java, Aug 26, 2026 nvquy
 */

import com.luvina.la.entity.CertificationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository truy vấn dữ liệu từ bảng certifications.
 *
 * @author quynv
 */
@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, Long> {

    /**
     * Lấy toàn bộ chứng chỉ sắp xếp theo cấp độ tăng dần (Level 1..5).
     *
     * @return Danh sách CertificationEntity
     */
    List<CertificationEntity> findAllByOrderByCertificationLevelAsc();
}
