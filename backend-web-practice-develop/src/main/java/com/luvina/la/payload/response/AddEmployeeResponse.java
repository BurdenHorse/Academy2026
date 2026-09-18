package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * AddEmployeeResponse.java, Aug 26, 2026 nvquy
 */

import java.util.Collections;
import java.util.List;
import com.luvina.la.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload response cho API thêm mới nhân viên (POST /employee).
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeeResponse {

    /** Mã HTTP status dạng String (ví dụ: "200") */
    private String code;

    /** ID của nhân viên vừa tạo */
    private Long employeeId;

    /** Thông tin thông báo kết quả */
    private MessageDetail message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDetail {
        private String code;
        private List<String> params;
    }

    /**
     * Tạo response thành công với MSG001.
     *
     * @param employeeId ID nhân viên vừa tạo
     * @return AddEmployeeResponse
     */
    public static AddEmployeeResponse success(Long employeeId) {
        return new AddEmployeeResponse(
                Constants.STATUS_CODE_SUCCESS,
                employeeId,
                new MessageDetail(Constants.MESSAGE_CODE_ADD_SUCCESS, Collections.emptyList())
        );
    }
}
