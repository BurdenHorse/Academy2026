package com.luvina.la.payload.request;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * LoginRequest.java, Aug 17, 2026 nvquy
 */

import lombok.Data;

/**
 * DTO chứa thông tin request đăng nhập (username và password).
 *
 * @author quynv
 */
@Data
public class LoginRequest {

    /** Tên đăng nhập (login id) */
    private String username;

    /** Mật khẩu người dùng */
    private String password;
}
