package com.luvina.la.controller;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationController.java, Aug 26, 2026 nvquy
 */

import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.mapper.CertificationMapper;
import com.luvina.la.payload.response.ListCertificationResponse;
import com.luvina.la.service.CertificationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller xử lý các request liên quan đến chứng chỉ tiếng Nhật.
 * Cung cấp API lấy danh sách chứng chỉ cho dropdown trên màn hình ADM004.
 * Endpoint: /certifications
 *
 * @author quynv
 */
@RestController
@RequestMapping({"/certifications", "/certification"})
public class CertificationController {

    private final CertificationService certificationService;
    private final CertificationMapper certificationMapper;

    public CertificationController(CertificationService certificationService, CertificationMapper certificationMapper) {
        this.certificationService = certificationService;
        this.certificationMapper = certificationMapper;
    }

    /**
     * API lấy danh sách tất cả chứng chỉ tiếng Nhật.
     * Endpoint: GET /certifications
     *
     * @return ListCertificationResponse chứa danh sách chứng chỉ
     */
    @GetMapping
    public ListCertificationResponse getCertifications() {
        List<CertificationDTO> certifications = certificationService.getAllCertifications();
        return certificationMapper.toListResponse(certifications);
    }
}
