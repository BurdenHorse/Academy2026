package com.luvina.la.util;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * DateTimeUtil.java, Sep 16, 2026
 */

import com.luvina.la.config.Constants;
import com.luvina.la.exception.AppException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Lớp tiện ích xử lý và định dạng ngày tháng (DateTime Utilities).
 */
public final class DateTimeUtil {

    /** Định dạng chuẩn cho ngày tháng trong toàn hệ thống */
    public static final String DATE_FORMAT = "yyyy/MM/dd";

    /** Biểu thức chính quy kiểm tra định dạng chuỗi ngày tháng yyyy/MM/dd */
    public static final Pattern DATE_FORMAT_PATTERN = Pattern.compile("^\\d{4}/\\d{2}/\\d{2}$");

    private DateTimeUtil() {
        // Utility class: private constructor to prevent instantiation
    }

    /**
     * Parse chuỗi ngày tháng dạng yyyy/MM/dd.
     * Phân biệt rõ:
     * - Sai định dạng yyyy/MM/dd -> ném AppException với mã lỗi ER005
     * - Đúng định dạng nhưng ngày không hợp lệ trên lịch (vd: 2023/02/30) -> ném AppException với mã lỗi ER011
     *
     * @param dateStr   Chuỗi ngày tháng cần parse
     * @param fieldName Tên trường bằng tiếng Nhật (dùng cho message lỗi)
     * @return Date hợp lệ sau khi parse
     */
    public static Date parseDate(String dateStr, String fieldName) {
        if (dateStr == null || !DATE_FORMAT_PATTERN.matcher(dateStr.trim()).matches()) {
            throw new AppException(Constants.ERROR_CODE_FORMAT_INVALID, Arrays.asList(fieldName, DATE_FORMAT));
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr.trim());
        } catch (ParseException e) {
            throw new AppException(Constants.ERROR_CODE_DATE_INVALID, Collections.singletonList(fieldName));
        }
    }

    /**
     * Parse chuỗi ngày tháng dạng yyyy/MM/dd sang Date.
     * Dùng khi chuỗi ngày tháng đã được kiểm tra tính hợp lệ trước đó.
     *
     * @param dateStr Chuỗi ngày tháng (yyyy/MM/dd)
     * @return Date hợp lệ sau khi parse, hoặc null nếu chuỗi rỗng/lỗi
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Chuyển đổi đối tượng Date sang chuỗi theo định dạng yyyy/MM/dd.
     *
     * @param date Đối tượng Date
     * @return Chuỗi ngày dạng yyyy/MM/dd hoặc chuỗi rỗng nếu date null
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }
}
