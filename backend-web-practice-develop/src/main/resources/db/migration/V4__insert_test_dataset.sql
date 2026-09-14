-- =========================================================================
-- V4: Cập nhật lại toàn bộ dữ liệu mẫu theo dataset kiểm thử
-- Reset hoàn toàn dữ liệu và AUTO_INCREMENT seed về 1
-- =========================================================================

-- Tạm thời tắt kiểm tra khóa ngoại để xóa và reset AUTO_INCREMENT sạch sẽ
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Xóa toàn bộ dữ liệu và reset seed AUTO_INCREMENT
TRUNCATE TABLE `employees_certifications`;
TRUNCATE TABLE `employees`;
TRUNCATE TABLE `certifications`;
TRUNCATE TABLE `departments`;

ALTER TABLE `departments` AUTO_INCREMENT = 1;
ALTER TABLE `certifications` AUTO_INCREMENT = 1;
ALTER TABLE `employees` AUTO_INCREMENT = 1;
ALTER TABLE `employees_certifications` AUTO_INCREMENT = 1;

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

-- 2. Bảng departments (Phòng ban)
INSERT INTO `departments` (`department_id`, `department_name`) VALUES 
(1, 'DEV1'),
(2, 'DEV2'),
(3, 'DEV3'),
(4, 'DEV4'),
(5, 'DEV5');

-- 3. Bảng certifications (Chứng chỉ)
INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES 
(1, 'Trình độ tiếng nhật cấp 1', 1),
(2, 'Trình độ tiếng nhật cấp 2', 2),
(3, 'Trình độ tiếng nhật cấp 3', 3),
(4, 'Trình độ tiếng nhật cấp 4', 4),
(5, 'Trình độ tiếng nhật cấp 5', 5);

-- 4. Bảng employees (Nhân viên)
-- Lưu ý: Mật khẩu BCrypt $2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy tương ứng với 'Luvina@123'
-- ID 1: Tài khoản Admin (role = 1)
-- ID 2 - 7: Tài khoản User kiểm thử ký tự đặc biệt LIKE (role = 0)
INSERT INTO `employees` (
  `employee_id`, `department_id`, `employee_name`, `employee_name_kana`, 
  `employee_birth_date`, `employee_email`, `employee_telephone`, 
  `employee_login_id`, `employee_login_password`, `role`
) VALUES 
(1, 1, 'Administrator', 'グループ', '2000-01-01', 'admin@luvina.net', '0123456789', 'admin', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 1),
(2, 1, 'Pham Thi Thanh Nga', 'グループ', '2002-02-02', 'ngantt@luvina.net', '778520123', 'ngaptt265', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(3, 2, 'QuỳnhNga/', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt266', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(4, 3, 'Quỳnh%Nga', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt267', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(5, 3, 'Quỳnh_Nga', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt268', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(6, 3, 'Quỳnh;Nga', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt269', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0),
(7, 4, 'Quỳnh,Nga', 'グループ', '2002-02-02', 'nga@luvina.net', '778520123', 'ngaptt270', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 0);

-- 5. Bảng employees_certifications (Chứng chỉ của nhân viên)
INSERT INTO `employees_certifications` (
  `employee_certification_id`, `employee_id`, `certification_id`, `start_date`, `end_date`, `score`
) VALUES 
(1, 1, 3, '2014-05-06', '2020-02-03', 77.00),
(2, 2, 5, '2015-03-06', '2021-03-04', 89.00),
(3, 3, 1, '2016-04-07', '2022-08-09', 123.00),
(4, 4, 2, '2017-05-04', '2024-06-09', 123.00),
(5, 5, 4, '2019-04-05', '2024-03-08', 124.00),
(6, 6, 4, '2017-02-06', '2025-02-02', 127.00),
(7, 7, 3, '2018-03-03', '2025-02-01', 145.00);
