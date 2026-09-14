package com.luvina.la.dto;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeListDTO.java, Aug 22, 2026 nvquy
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO phẳng đại diện cho một bản ghi nhân viên trong danh sách (ADM002).
 * Cấu trúc phẳng bao gồm thông tin nhân viên kèm chứng chỉ (nếu có),
 * mỗi bản ghi tương ứng một dòng trong bảng hiển thị.
 * Tất cả các trường đều là String theo đúng specification API.
 *
 * @author quynv
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Định dạng ngày tháng theo specification (thread-safe) */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /** ID nhân viên */
    private String employeeId;

    /** Họ và tên nhân viên */
    private String employeeName;

    /** Ngày sinh (format yyyy/MM/dd) */
    private String employeeBirthDate;

    /** Tên phòng ban */
    private String departmentName;

    /** Địa chỉ email */
    private String employeeEmail;

    /** Số điện thoại */
    private String employeeTelephone;

    /** Tên chứng chỉ tiếng Nhật (null nếu không có chứng chỉ) */
    private String certificationName;

    /** Ngày hết hạn chứng chỉ (format yyyy/MM/dd, null nếu không có chứng chỉ) */
    private String endDate;

    /** Điểm thi chứng chỉ (null nếu không có chứng chỉ) */
    private String score;

    /**
     * Constructor tạo DTO từ kết quả truy vấn database khi các trường đã ở dạng String.
     * Thường dùng cho Native SQL với hàm DATE_FORMAT().
     *
     * @param employeeId        ID nhân viên
     * @param employeeName      Họ và tên nhân viên
     * @param employeeBirthDate Ngày sinh dạng chuỗi yyyy/MM/dd
     * @param departmentName    Tên phòng ban
     * @param employeeEmail     Địa chỉ email
     * @param employeeTelephone Số điện thoại
     * @param certificationName Tên chứng chỉ (có thể null)
     * @param endDate           Ngày hết hạn chứng chỉ dạng chuỗi yyyy/MM/dd (có thể null)
     * @param score             Điểm thi chứng chỉ dạng chuỗi (có thể null)
     */
    public EmployeeListDTO(String employeeId, String employeeName, String employeeBirthDate,
                           String departmentName, String employeeEmail, String employeeTelephone,
                           String certificationName, String endDate, String score) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeBirthDate = employeeBirthDate;
        this.departmentName = departmentName;
        this.employeeEmail = employeeEmail;
        this.employeeTelephone = employeeTelephone;
        this.certificationName = certificationName;
        this.endDate = endDate;
        this.score = score;
    }

    /**
     * Constructor tạo DTO từ kết quả truy vấn database (Date format).
     *
     * @param employeeId    ID nhân viên (Long từ DB)
     * @param employeeName  Họ và tên nhân viên
     * @param employeeBirthDate Ngày sinh (Date từ DB)
     * @param departmentName    Tên phòng ban
     * @param employeeEmail     Địa chỉ email
     * @param employeeTelephone Số điện thoại
     * @param certificationName Tên chứng chỉ (có thể null nếu không có)
     * @param endDate           Ngày hết hạn chứng chỉ (có thể null)
     * @param score             Điểm thi chứng chỉ (có thể null)
     */
    public EmployeeListDTO(Long employeeId, String employeeName, Date employeeBirthDate,
                           String departmentName, String employeeEmail, String employeeTelephone,
                           String certificationName, Date endDate, Integer score) {
        this.employeeId = employeeId != null ? String.valueOf(employeeId) : null;
        this.employeeName = employeeName;
        this.employeeBirthDate = formatDate(employeeBirthDate);
        this.departmentName = departmentName;
        this.employeeEmail = employeeEmail;
        this.employeeTelephone = employeeTelephone;
        this.certificationName = certificationName;
        this.endDate = formatDate(endDate);
        this.score = score != null ? String.valueOf(score) : null;
    }

    /**
     * Format java.util.Date sang chuỗi yyyy/MM/dd sử dụng DateTimeFormatter (thread-safe).
     *
     * @param date Ngày cần format (có thể null)
     * @return Chuỗi ngày đã format hoặc null nếu input null
     */
    private static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DATE_FORMATTER);
    }
}
