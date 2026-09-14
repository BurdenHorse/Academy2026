package com.luvina.la.handler;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * GlobalExceptionHandler.java, Aug 22, 2026 nvquy
 */

import com.luvina.la.config.Constants;
import com.luvina.la.exception.AppException;
import com.luvina.la.payload.response.ErrorResponse;
import com.luvina.la.payload.response.MessageResponse;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Xử lý ngoại lệ toàn cục cho toàn bộ ứng dụng.
 * Bắt các exception và trả về response theo đúng format specification:
 * {"code": "xxx", "message": {"code": "ERxxx", "params": [...]}}.
 *
 * @author quynv
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Xử lý AppException — lỗi nghiệp vụ (validation tham số, lỗi business logic).
     * Trả về HTTP 500 với body theo format specification.
     *
     * @param ex AppException chứa mã lỗi và danh sách tham số
     * @return ResponseEntity chứa ErrorResponse theo đúng format spec
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        log.warn("Lỗi nghiệp vụ: {} - params: {}", ex.getErrorCode(), ex.getParams());

        MessageResponse message = new MessageResponse();
        message.setCode(ex.getErrorCode());
        message.setParams(ex.getParams());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(Constants.STATUS_CODE_ERROR);
        errorResponse.setMessage(message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Xử lý các exception không xác định — lỗi hệ thống.
     * Trả về HTTP 500 với mã lỗi ER023 (System Error).
     *
     * @param ex Exception không xác định
     * @return ResponseEntity chứa ErrorResponse với mã ER023
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Lỗi hệ thống không xác định: ", ex);

        MessageResponse message = new MessageResponse();
        message.setCode(Constants.ERROR_CODE_SYSTEM);
        message.setParams(Collections.emptyList());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(Constants.STATUS_CODE_ERROR);
        errorResponse.setMessage(message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
