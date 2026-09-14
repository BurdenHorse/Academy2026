package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * ErrorResponse.java, Aug 22, 2026 nvquy
 */

import lombok.Data;

/**
 * DTO response khi xảy ra lỗi.
 * Format theo specification: {"code": "500", "message": {"code": "ERxxx", "params": [...]}}.
 *
 * @author quynv
 */
@Data
public class ErrorResponse {

    /** Mã HTTP status dạng String (ví dụ: "500", "400") */
    private String code;

    /** Chi tiết lỗi bao gồm mã lỗi nghiệp vụ và danh sách tham số */
    private MessageResponse message;
}
