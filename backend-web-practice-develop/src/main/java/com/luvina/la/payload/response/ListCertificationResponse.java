package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * ListCertificationResponse.java, Aug 26, 2026 nvquy
 */

import com.luvina.la.dto.CertificationDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload Response cho API lấy danh sách chứng chỉ tiếng Nhật (GET /certifications).
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListCertificationResponse {

    /** Mã trạng thái dạng String (ví dụ: "200") */
    private String code;

    /** Danh sách chứng chỉ tiếng Nhật */
    private List<CertificationDTO> certifications;
}
