package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * AddEmployeeDTO.java, Sep 08, 2026 nvquy
 */

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO trả về từ Service sau khi thêm mới nhân viên thành công.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID của nhân viên vừa được tạo */
    private Long employeeId;
}
