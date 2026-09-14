package com.luvina.la.config.jwt;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * UserDetailsServiceImpl.java, Aug 17, 2026 nvquy
 */

import com.luvina.la.config.Constants;
import com.luvina.la.entity.EmployeeEntity;
import com.luvina.la.repository.EmployeeRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Triển khai UserDetailsService của Spring Security để tải thông tin người dùng
 * từ database theo tên đăng nhập (employee_login_id) và phân quyền tương ứng.
 *
 * @author quynv
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepository userRepository;

    /**
     * Constructor injection cho EmployeeRepository.
     *
     * @param userRepository Repository truy vấn dữ liệu nhân viên
     */
    public UserDetailsServiceImpl(EmployeeRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Tìm nhân viên theo tên đăng nhập và phân quyền:
     * - role = 1 (Constants.ROLE_ADMIN) ➔ ROLE_ADMIN
     * - role = 0 (Constants.ROLE_USER) ➔ ROLE_USER</p>
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EmployeeEntity> entity = this.userRepository.findByEmployeeLoginId(username);

        if (entity.isPresent()) {
            EmployeeEntity employee = entity.get();
            String roleName = (employee.getRole() != null && employee.getRole() == Constants.ROLE_ADMIN)
                    ? "ROLE_ADMIN"
                    : "ROLE_USER";
            Collection<GrantedAuthority> roles = Collections.singleton(new SimpleGrantedAuthority(roleName));
            return new AuthUserDetails(employee, roles);
        } else {
            throw new UsernameNotFoundException("Employee not found with username: " + username);
        }
    }
}
