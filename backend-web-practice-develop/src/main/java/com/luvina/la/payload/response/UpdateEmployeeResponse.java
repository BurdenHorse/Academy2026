package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * UpdateEmployeeResponse.java, Sep 10, 2026 nvquy
 */

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload response cho API cập nhật thông tin nhân viên (PUT /employee).
 * Theo đặc tả kỹ thuật TKAPI_Tong_hop.md - Section 7.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeResponse {

    /** Mã HTTP status dạng String (ví dụ: "200") */
    private String code;

    /** ID của nhân viên vừa cập nhật */
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
     * Tạo response cập nhật thành công với mã MSG002.
     *
     * @param employeeId ID nhân viên vừa cập nhật
     * @return UpdateEmployeeResponse
     */
    public static UpdateEmployeeResponse success(Long employeeId) {
        return new UpdateEmployeeResponse(
                "200",
                employeeId,
                new MessageDetail("MSG002", Collections.emptyList())
        );
    }
}
