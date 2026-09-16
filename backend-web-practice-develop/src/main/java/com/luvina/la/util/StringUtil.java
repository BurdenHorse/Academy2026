package com.luvina.la.util;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * StringUtil.java, Sep 16, 2026
 */

import java.math.BigDecimal;

/**
 * Lớp tiện ích xử lý chuỗi và định dạng hiển thị (String Utilities).
 */
public final class StringUtil {

    private StringUtil() {
        // Utility class: private constructor to prevent instantiation
    }

    /**
     * Kiểm tra chuỗi có null hoặc chỉ chứa khoảng trắng hay không.
     *
     * @param str Chuỗi cần kiểm tra
     * @return true nếu null hoặc rỗng sau khi trim
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Escape các ký tự đặc biệt của SQL LIKE (%, _, \) để chống wildcard injection.
     *
     * @param input Chuỗi tìm kiếm ban đầu
     * @return Chuỗi đã được escape an toàn
     */
    public static String escapeLikeWildcards(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
    }

    /**
     * Format điểm số chứng chỉ: loại bỏ đuôi .00 hoặc .0 thừa nếu có.
     * Ví dụ: 180.00 -> 180, 150.0 -> 150.
     *
     * @param scoreObj Đối tượng điểm từ DB hoặc request
     * @return Chuỗi điểm số đã format
     */
    public static String formatScore(Object scoreObj) {
        if (scoreObj == null) {
            return null;
        }
        if (scoreObj instanceof BigDecimal) {
            return ((BigDecimal) scoreObj).stripTrailingZeros().toPlainString();
        }
        String scoreStr = String.valueOf(scoreObj).trim();
        if (scoreStr.endsWith(".00")) {
            return scoreStr.substring(0, scoreStr.length() - 3);
        }
        if (scoreStr.endsWith(".0")) {
            return scoreStr.substring(0, scoreStr.length() - 2);
        }
        return scoreStr;
    }

    /**
     * Trim chuỗi và trả về null nếu chuỗi là null hoặc chỉ chứa khoảng trắng.
     *
     * @param str Chuỗi cần trim
     * @return Chuỗi đã trim hoặc null nếu chuỗi rỗng
     */
    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }
        String trimmed = str.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Chuẩn hóa giá trị sắp xếp (ASC/DESC): trim và chuyển sang chữ hoa.
     * Trả về null nếu giá trị là null hoặc rỗng.
     *
     * @param sortValue Giá trị tham số sort
     * @return Chuỗi viết hoa ("ASC", "DESC") hoặc null nếu không có giá trị
     */
    public static String normalizeSort(String sortValue) {
        String trimmed = trimToNull(sortValue);
        return trimmed != null ? trimmed.toUpperCase() : null;
    }

    /**
     * Chuyển đổi an toàn chuỗi sang kiểu Long.
     * Trả về null nếu chuỗi là null, rỗng hoặc không phải định dạng số hợp lệ (không ném Exception).
     *
     * @param str Chuỗi cần parse
     * @return Giá trị Long hoặc null nếu không hợp lệ
     */
    public static Long toLongOrNull(String str) {
        String trimmed = trimToNull(str);
        if (trimmed == null) {
            return null;
        }
        try {
            return Long.parseLong(trimmed);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tách chuỗi theo ký tự phân cách (delimiter), tự động trim từng phần tử
     * và loại bỏ các phần tử rỗng.
     *
     * @param input     Chuỗi đầu vào (ví dụ "ord_name, ord_date")
     * @param delimiter Ký tự phân cách (ví dụ ",")
     * @return Danh sách các phần tử đã được trim và không rỗng
     */
    public static java.util.List<String> splitAndTrim(String input, String delimiter) {
        if (isNullOrEmpty(input)) {
            return java.util.Collections.emptyList();
        }
        return java.util.Arrays.stream(input.split(java.util.regex.Pattern.quote(delimiter)))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }
}
