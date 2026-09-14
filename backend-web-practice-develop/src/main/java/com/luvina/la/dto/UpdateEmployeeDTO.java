package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * UpdateEmployeeDTO.java, Sep 10, 2026 nvquy
 */

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO trả về từ Service sau khi cập nhật thông tin nhân viên thành công.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID của nhân viên vừa được cập nhật */
    private Long employeeId;
}
