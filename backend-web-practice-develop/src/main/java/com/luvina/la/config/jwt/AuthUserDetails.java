package com.luvina.la.config.jwt;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * AuthUserDetails.java, Aug 17, 2026 nvquy
 */

import java.util.Collection;
import com.luvina.la.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Adapter triển khai Spring Security UserDetails bọc EmployeeEntity.
 * Cung cấp thông tin xác thực và phân quyền cho người dùng.
 *
 * @author quynv
 */
@Data
@AllArgsConstructor
public class AuthUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    /** Entity nhân viên được xác thực */
    private EmployeeEntity employee;

    /** Danh sách quyền hạn được cấp */
    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return employee.getEmployeeLoginPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmployeeLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
