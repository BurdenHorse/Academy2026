package com.luvina.la.entity;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeEntity.java, Aug 17, 2026 nvquy
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Entity đại diện cho bảng employees, lưu thông tin nhân viên.
 * Mapping theo đúng TKDB specification:
 * - employee_name: VARCHAR(255)
 * - employee_email: VARCHAR(255)
 * - employee_telephone: VARCHAR(50)
 *
 * @author quynv
 */
@Entity
@Table(name = "employees")
@Data
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 5771173953267484096L;

    /** ID nhân viên, tự động tăng (Primary Key) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true)
    private Long employeeId;

    /** ID phòng ban của nhân viên (Ghi trực tiếp vào DB, không cần query Entity) */
    @Column(name = "department_id")
    private Long departmentId;

    /** Thông tin phòng ban liên kết (Chỉ dùng để ĐỌC dữ liệu) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private DepartmentEntity department;

    /** Tên nhân viên — VARCHAR(255) theo TKDB */
    @Column(name = "employee_name", length = 255, nullable = false)
    private String employeeName;

    /** Tên nhân viên dạng kana — VARCHAR(255) theo TKDB */
    @Column(name = "employee_name_kana", length = 255)
    private String employeeNameKana;

    /** Ngày sinh */
    @Column(name = "employee_birth_date")
    @Temporal(TemporalType.DATE)
    private Date employeeBirthDate;

    /** Địa chỉ email — VARCHAR(255) theo TKDB */
    @Column(name = "employee_email", length = 255, nullable = false)
    private String employeeEmail;

    /** Số điện thoại — VARCHAR(50) theo TKDB */
    @Column(name = "employee_telephone", length = 50)
    private String employeeTelephone;

    /** Login ID cho đăng nhập */
    @Column(name = "employee_login_id", length = 50, nullable = false)
    private String employeeLoginId;

    /** Mật khẩu đăng nhập — đánh dấu @JsonIgnore để không serialize ra JSON, tránh lộ thông tin */
    @JsonIgnore
    @Column(name = "employee_login_password", length = 50)
    private String employeeLoginPassword;

    /** Vai trò của nhân viên: 0: User, 1: Admin */
    @Column(name = "role", nullable = false)
    private Integer role = 0;

    /** Danh sách chứng chỉ của nhân viên (quan hệ 1-N với employees_certifications) */
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<EmployeeCertificationEntity> employeeCertifications;
}
