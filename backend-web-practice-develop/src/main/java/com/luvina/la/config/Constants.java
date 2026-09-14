package com.luvina.la.config;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * Constants.java, Aug 17, 2026 nvquy
 */

/**
 * Lớp khai báo tất cả các hằng số dùng chung trong ứng dụng.
 * Bao gồm: cấu hình Spring profile, JWT, security endpoints,
 * error codes, status codes, pagination defaults, sort values, param names và roles.
 *
 * @author quynv
 */
public class Constants {

    private Constants() {
    }

    // === Spring Profile ===
    /** Profile môi trường phát triển */
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

    /** Profile môi trường sản phẩm */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    /** Cho phép CORS hay không */
    public static final boolean IS_CROSS_ALLOW = true;

    // === JWT Configuration ===
    /** Secret key cho JWT */
    public static final String JWT_SECRET = "Luvina-Academe";

    /** Thời gian hết hạn JWT (giây) */
    public static final long JWT_EXPIRATION = 160 * 60 * 60; // 7 day

    // === Security Endpoints ===
    /** Các endpoint public không cần authentication */
    public static final String[] ENDPOINTS_PUBLIC = new String[] {
            "/",
            "/login/**",
            "/error/**"
    };

    /** Các endpoint yêu cầu role USER */
    public static final String[] ENDPOINTS_WITH_ROLE = new String[] {
            "/user/**"
    };

    /** Các thuộc tính user đưa vào JWT token */
    public static final String[] ATTRIBUTIES_TO_TOKEN = new String[] {
            "employeeId",
            "employeeName",
            "employeeLoginId",
            "employeeEmail",
            "role"
    };

    // === User Roles ===
    /** Vai trò người dùng thông thường */
    public static final int ROLE_USER = 0;

    /** Vai trò quản trị viên */
    public static final int ROLE_ADMIN = 1;

    // === Error Codes ===
    /** Mã lỗi bắt buộc nhập (ER001) */
    public static final String ERROR_CODE_REQUIRED = "ER001";

    /** Mã lỗi bắt buộc chọn (ER002) */
    public static final String ERROR_CODE_SELECT_REQUIRED = "ER002";

    /** Mã lỗi đã tồn tại trong hệ thống (ER003) */
    public static final String ERROR_CODE_ALREADY_EXISTS = "ER003";

    /** Mã lỗi không tồn tại trong hệ thống (ER004) */
    public static final String ERROR_CODE_NOT_FOUND = "ER004";

    /** Mã lỗi định dạng không hợp lệ (ER005) */
    public static final String ERROR_CODE_FORMAT_INVALID = "ER005";

    /** Mã lỗi độ dài vượt quá giới hạn tối đa (ER006) */
    public static final String ERROR_CODE_MAX_LENGTH = "ER006";

    /** Mã lỗi độ dài ký tự trong khoảng min - max (ER007) */
    public static final String ERROR_CODE_LENGTH_RANGE = "ER007";

    /** Mã lỗi ký tự halfsize (ER008) */
    public static final String ERROR_CODE_HALFSIZE = "ER008";

    /** Mã lỗi ký tự Katakana (ER009) */
    public static final String ERROR_CODE_KANA = "ER009";

    /** Mã lỗi ngày tháng không hợp lệ trên lịch (ER011) */
    public static final String ERROR_CODE_DATE_INVALID = "ER011";

    /** Mã lỗi ngày hết hạn phải sau ngày cấp (ER012) */
    public static final String ERROR_CODE_END_DATE_INVALID = "ER012";

    /** Mã lỗi user không tồn tại khi xem chi tiết / cập nhật (ER013) */
    public static final String ERROR_CODE_USER_NOT_FOUND_GET = "ER013";

    /** Mã lỗi user không tồn tại khi xóa (ER014) */
    public static final String ERROR_CODE_USER_NOT_FOUND_DELETE = "ER014";

    /** Mã lỗi thao tác thất bại / lỗi hệ thống (ER015) */
    public static final String ERROR_CODE_DELETE_FAILED = "ER015";

    /** Mã lỗi đăng nhập thất bại (ER016) */
    public static final String ERROR_CODE_LOGIN_FAILED = "ER016";

    /** Mã lỗi mật khẩu xác nhận không khớp (ER017) */
    public static final String ERROR_CODE_PASSWORD_CONFIRM = "ER017";

    /** Mã lỗi số halfsize / số nguyên dương không hợp lệ (ER018) */
    public static final String ERROR_CODE_HALFSIZE_NUMBER = "ER018";

    /** Mã lỗi định dạng tên đăng nhập không hợp lệ (ER019) */
    public static final String ERROR_CODE_LOGIN_ID_FORMAT = "ER019";

    /** Mã lỗi không thể xóa tài khoản Admin (ER020) */
    public static final String ERROR_CODE_CANNOT_DELETE_ADMIN = "ER020";

    /** Mã lỗi thứ tự sắp xếp không hợp lệ (ER021) */
    public static final String ERROR_CODE_SORT_INVALID = "ER021";

    /** Mã lỗi hệ thống chung (ER023) */
    public static final String ERROR_CODE_SYSTEM = "ER023";

    // === Message Codes ===
    /** Mã thông báo thêm nhân viên thành công (MSG001) */
    public static final String MESSAGE_CODE_ADD_SUCCESS = "MSG001";

    /** Mã thông báo cập nhật nhân viên thành công (MSG002) */
    public static final String MESSAGE_CODE_UPDATE_SUCCESS = "MSG002";

    /** Mã thông báo xóa thành công (MSG003) */
    public static final String MESSAGE_CODE_DELETE_SUCCESS = "MSG003";

    /** Mã thông báo xác nhận xóa (MSG004) */
    public static final String MESSAGE_CODE_CONFIRM_DELETE = "MSG004";

    /** Mã thông báo không tìm thấy bản ghi (MSG005) */
    public static final String MESSAGE_CODE_NO_DATA = "MSG005";

    // === Response Status Codes ===
    /** Mã trạng thái thành công */
    public static final String STATUS_CODE_SUCCESS = "200";

    /** Mã trạng thái lỗi server */
    public static final String STATUS_CODE_ERROR = "500";

    // === Default Pagination ===
    /** Giá trị mặc định cho offset (vị trí bắt đầu) */
    public static final int DEFAULT_OFFSET = 0;

    /** Giá trị mặc định cho limit (số bản ghi mỗi trang) */
    public static final int DEFAULT_LIMIT = 5;

    // === Validation Constraints ===
    /** Độ dài tối đa cho tên nhân viên tìm kiếm */
    public static final int MAX_LENGTH_EMPLOYEE_NAME = 125;

    // === Sort Valid Values ===
    /** Giá trị sort tăng dần */
    public static final String SORT_ASC = "ASC";

    /** Giá trị sort giảm dần */
    public static final String SORT_DESC = "DESC";

    // === Japanese Param Names (dùng cho message lỗi) ===
    /** Tên tham số offset bằng tiếng Nhật */
    public static final String PARAM_NAME_OFFSET = "オフセット";

    /** Tên tham số limit bằng tiếng Nhật */
    public static final String PARAM_NAME_LIMIT = "リミット";

    /** Tên tham số họ tên bằng tiếng Nhật */
    public static final String PARAM_NAME_EMPLOYEE_NAME = "氏名";

    /** Tên tham số tên đăng nhập bằng tiếng Nhật */
    public static final String PARAM_NAME_ACCOUNT = "アカウント名";

    /** Tên tham số họ tên Katakana bằng tiếng Nhật */
    public static final String PARAM_NAME_FULL_NAME_KANA = "カタカナ氏名";

    /** Tên tham số ngày sinh bằng tiếng Nhật */
    public static final String PARAM_NAME_BIRTH_DATE = "生年月日";

    /** Tên tham số email bằng tiếng Nhật */
    public static final String PARAM_NAME_EMAIL = "メールアドレス";

    /** Tên tham số số điện thoại bằng tiếng Nhật */
    public static final String PARAM_NAME_TELEPHONE = "電話番号";

    /** Tên tham số mật khẩu bằng tiếng Nhật */
    public static final String PARAM_NAME_PASSWORD = "パスワード";

    /** Tên tham số phòng ban bằng tiếng Nhật */
    public static final String PARAM_NAME_GROUP = "グループ";

    /** Tên tham số chứng chỉ bằng tiếng Nhật */
    public static final String PARAM_NAME_CERTIFICATION = "資格";

    /** Tên tham số ngày cấp chứng chỉ bằng tiếng Nhật */
    public static final String PARAM_NAME_CERT_START_DATE = "資格交付日";

    /** Tên tham số ngày hết hạn chứng chỉ bằng tiếng Nhật */
    public static final String PARAM_NAME_CERT_END_DATE = "失効日";

    /** Tên tham số điểm chứng chỉ bằng tiếng Nhật */
    public static final String PARAM_NAME_SCORE = "点数";

    /** Tên tham số ID bằng tiếng Nhật (Full-width) */
    public static final String PARAM_NAME_ID = "ＩＤ";
}
