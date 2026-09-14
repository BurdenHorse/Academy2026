package com.luvina.la.controller;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * HomeController.java, Aug 21, 2026 nvquy
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller xử lý request kiểm tra trạng thái hoạt động của server tại trang chủ.
 *
 * @author quynv
 */
@RestController
public class HomeController {

    /**
     * Endpoint mặc định kiểm tra dịch vụ.
     *
     * @return Chuỗi chào mừng xác nhận service đang chạy
     */
    @GetMapping("/")
    public String index() {
        return "Welcome to Employee service";
    }
}
