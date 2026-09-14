-- Thêm cột role vào bảng employees (0: User, 1: Admin)
ALTER TABLE `employees` 
ADD COLUMN `role` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '0: User, 1: Admin' AFTER `employee_login_password`;

-- Cập nhật tài khoản admin mặc định sang role 1 (Admin)
UPDATE `employees` SET `role` = 1 WHERE `employee_login_id` = 'admin';
