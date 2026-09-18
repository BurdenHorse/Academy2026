package com.luvina.la.service.impl;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * CertificationServiceImpl.java, Aug 26, 2026 nvquy
 */

import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.entity.CertificationEntity;
import com.luvina.la.mapper.CertificationMapper;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.service.CertificationService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lớp triển khai CertificationService, xử lý nghiệp vụ liên quan đến chứng chỉ tiếng Nhật.
 *
 * @author quynv
 */
@Service
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final CertificationMapper certificationMapper;

    public CertificationServiceImpl(CertificationRepository certificationRepository,
                                    CertificationMapper certificationMapper) {
        this.certificationRepository = certificationRepository;
        this.certificationMapper = certificationMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CertificationDTO> getAllCertifications() {
        List<CertificationEntity> certificationEntities = certificationRepository.findAllByOrderByCertificationLevelAsc();
        return certificationMapper.toDTOList(certificationEntities);
    }
}
