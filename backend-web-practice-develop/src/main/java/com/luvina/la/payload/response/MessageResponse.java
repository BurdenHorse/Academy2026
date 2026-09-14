package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * MessageResponse.java, Aug 22, 2026 nvquy
 */

import java.util.List;
import lombok.Data;

/**
 * DTO chứa chi tiết thông báo lỗi, nằm bên trong ErrorResponse.
 * Format theo specification: {"code": "ERxxx", "params": [...]}.
 *
 * @author quynv
 */
@Data
public class MessageResponse {

    /** Mã lỗi nghiệp vụ (ví dụ: ER015, ER018, ER021, ER023) */
    private String code;

    /** Danh sách tham số liên quan đến lỗi */
    private List<String> params;
}
