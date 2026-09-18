package com.luvina.la.payload.response;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DeleteEmployeeResponse.java, Sep 08, 2026 nvquy
 */

import java.util.Collections;
import java.util.List;
import com.luvina.la.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload response cho API xóa nhân viên (DELETE /employee/{id}).
 * Theo đặc tả kỹ thuật TKAPI_Tong_hop.md - Section 8.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteEmployeeResponse {

    /** Mã HTTP status dạng String (ví dụ: "200") */
    private String code;

    /** ID của nhân viên vừa xóa */
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
     * Tạo response xóa thành công với MSG003.
     *
     * @param employeeId ID nhân viên vừa xóa
     * @return DeleteEmployeeResponse
     */
    public static DeleteEmployeeResponse success(Long employeeId) {
        return new DeleteEmployeeResponse(
                Constants.STATUS_CODE_SUCCESS,
                employeeId,
                new MessageDetail(Constants.MESSAGE_CODE_DELETE_SUCCESS, Collections.emptyList())
        );
    }
}
