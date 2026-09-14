package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DeleteEmployeeDTO.java, Sep 08, 2026 nvquy
 */

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO trả về từ Service sau khi xóa nhân viên thành công.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteEmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ID của nhân viên vừa xóa */
    private Long employeeId;
}
