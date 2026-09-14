package com.luvina.la.controller;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * AuthController.java, Aug 17, 2026 nvquy
 */

import com.luvina.la.config.jwt.AuthUserDetails;
import com.luvina.la.config.jwt.JwtTokenProvider;
import com.luvina.la.config.jwt.UserDetailsServiceImpl;
import com.luvina.la.payload.request.LoginRequest;
import com.luvina.la.payload.response.LoginResponse;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller xử lý xác thực và phân quyền (Authentication / Authorization).
 * Cung cấp API đăng nhập và kiểm tra tính hợp lệ của JWT token.
 *
 * @author quynv
 */
@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructor injection cho các dependencies của AuthController.
     *
     * @param authenticationManager Manager thực hiện xác thực thông tin đăng nhập
     * @param jwtTokenProvider      Provider tạo và xác thực JWT token
     * @param userDetailsService    Service lấy thông tin chi tiết người dùng
     */
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * API đăng nhập hệ thống.
     * Nhận username, password và trả về JWT access token nếu thành công.
     *
     * @param loginRequest DTO chứa thông tin đăng nhập (username, password)
     * @param request      HttpServletRequest
     * @return LoginResponse chứa access token hoặc map lỗi
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Map<String, String> errorResponse = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = tokenProvider.generateToken((AuthUserDetails) authentication.getPrincipal());
            return new LoginResponse(accessToken);
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            log.warn("Đăng nhập thất bại do sai thông tin xác thực: {}", ex.getMessage());
            errorResponse.put("code", "100");
        } catch (Exception ex) {
            log.warn("Lỗi không xác định khi đăng nhập: ", ex);
            errorResponse.put("code", "000");
        }
        return new LoginResponse(errorResponse);
    }

    /**
     * API kiểm tra token có hợp lệ không.
     *
     * @return Map chứa message xác nhận token hợp lệ
     */
    @RequestMapping("/test-auth")
    public Map<String, String> testAuth() {
        Map<String, String> testAuthResponse = new HashMap<>();
        testAuthResponse.put("msg", "Token is valid");
        return testAuthResponse;
    }
}
