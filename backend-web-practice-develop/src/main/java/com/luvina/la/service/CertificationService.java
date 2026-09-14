package com.luvina.la.service;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationService.java, Aug 26, 2026 nvquy
 */

import com.luvina.la.dto.CertificationDTO;
import java.util.List;

/**
 * Interface service xử lý nghiệp vụ liên quan đến chứng chỉ tiếng Nhật.
 *
 * @author quynv
 */
public interface CertificationService {

    /**
     * Lấy danh sách tất cả các chứng chỉ tiếng Nhật trong hệ thống.
     *
     * @return Danh sách CertificationDTO
     */
    List<CertificationDTO> getAllCertifications();
}
