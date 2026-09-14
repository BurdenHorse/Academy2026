package com.luvina.la.exception;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * AppException.java, Aug 22, 2026 nvquy
 */

import java.util.Collections;
import java.util.List;

/**
 * Exception tùy chỉnh cho ứng dụng.
 * Chứa mã lỗi và danh sách tham số theo specification error response.
 * Được sử dụng để ném lỗi nghiệp vụ (validation, business logic)
 * và được bắt bởi GlobalExceptionHandler để trả response đúng format.
 *
 * @author quynv
 */
public class AppException extends RuntimeException {

    /** Mã lỗi nghiệp vụ (ví dụ: ER015, ER018, ER021) */
    private final String errorCode;

    /** Danh sách tham số kèm theo lỗi */
    private final List<String> params;

    /**
     * Tạo exception với mã lỗi và danh sách tham số.
     *
     * @param errorCode Mã lỗi nghiệp vụ (ví dụ: ER015, ER018, ER021)
     * @param params    Danh sách tham số liên quan đến lỗi
     */
    public AppException(String errorCode, List<String> params) {
        super(errorCode);
        this.errorCode = errorCode;
        this.params = params != null ? params : Collections.emptyList();
    }

    /**
     * Tạo exception với mã lỗi, không có tham số.
     *
     * @param errorCode Mã lỗi nghiệp vụ
     */
    public AppException(String errorCode) {
        this(errorCode, Collections.emptyList());
    }

    /**
     * Lấy mã lỗi nghiệp vụ.
     *
     * @return Mã lỗi (ví dụ: ER015, ER018, ER021)
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Lấy danh sách tham số kèm theo lỗi.
     *
     * @return Danh sách tham số
     */
    public List<String> getParams() {
        return params;
    }
}
