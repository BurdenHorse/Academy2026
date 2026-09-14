-- =========================================================================
-- V5: Khôi phục lại toàn bộ dữ liệu giống V2, bổ sung trường `role` cho employees
-- (Tài khoản 'admin' có role = 1, các nhân viên còn lại có role = 0)
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

-- 2. Bảng departments (Phòng ban 1 - 8)
INSERT INTO `departments` (`department_id`, `department_name`) VALUES (1, 'DEV1');
INSERT INTO `departments` (`department_id`, `department_name`) VALUES (2, 'DEV2');
INSERT INTO `departments` (`department_id`, `department_name`) VALUES (3, 'DEV3');
INSERT INTO `departments` (`department_id`, `department_name`) VALUES (4, 'DEV4');
INSERT INTO `departments` (`department_id`, `department_name`) VALUES (5, 'DEV5');
INSERT INTO `departments` (`department_id`, `department_name`) VALUES (6, 'DEV6');
INSERT INTO `departments` (`department_id`, `department_name`) VALUES (7, 'DEV7');
INSERT INTO `departments` (`department_id`, `department_name`) VALUES (8, 'DEV8');

-- 3. Bảng certifications (Chứng chỉ 1 - 5)
INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES (1, 'Trình độ tiếng nhật cấp 1', 1);
INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES (2, 'Trình độ tiếng nhật cấp 2', 2);
INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES (3, 'Trình độ tiếng nhật cấp 3', 3);
INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES (4, 'Trình độ tiếng nhật cấp 4', 4);
INSERT INTO `certifications` (`certification_id`, `certification_name`, `certification_level`) VALUES (5, 'Trình độ tiếng nhật cấp 5', 5);

-- 4. Bảng employees (Nhân viên: admin role = 1, các nhân viên role = 0)
INSERT INTO `employees` (`employee_id`, `department_id`, `employee_name`, `employee_email`, `employee_login_id`, `employee_login_password`, `role`)
VALUES (999, 1, 'Administrator', 'la@luvina.net', 'admin', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy', 1);

INSERT INTO `employees` (`employee_id`, `department_id`, `employee_name`, `employee_name_kana`, `employee_birth_date`, `employee_email`, `employee_telephone`, `employee_login_id`, `employee_login_password`, `role`) VALUES 
(1, 1, 'ngaptt', NULL, '1989-05-26', 'phamthithanhnga@luvina.net', '122478954', 'ngaptt265', '$2a$10$Di2yM..T/YqExIRQ83ulYeVr7PEauMaeOZqRA3bF3ZcwazVdGGYqK', 0),
(2, 1, 'minhdm', NULL, '2006-12-02', 'minhdm@luvina.net', '822276354', 'minhdm01', '$2a$10$23eZZBaPEH/8IEzbGWxZIODTBT04W1GbJPH9ZuEDS.FbYGkUMH.0G', 0),
(3, 2, 'huyndq', NULL, '2006-12-03', 'huyndq@luvina.net', '122480927', 'huyndq02', '$2a$10$YfeEEPTaPL9c15.NhJInnezS3Ji16uj90OOgjEGaC4SkJC/.7AslG', 0),
(4, 3, 'vinhbt', NULL, '2006-12-04', 'vinhbt@luvina.net', '122480928', 'vinhbt03', '$2a$10$3CTXhnoSkYpxuEtWWEbc9.1JfCoQhhsVFiPNQKxyMQNik4AosiJ8m', 0),
(5, 4, 'tienhn', NULL, '2006-12-05', 'tienhn@luvina.net', '122480929', 'tienhn04', '$2a$10$ZgwMAPNqeGneod9KRCakjekQ.LgMPQh9tHRiBCUZnXtDu7OuGpmAa', 0),
(6, 5, 'thanhdn', NULL, '2006-12-06', 'thanhdn@luvina.net', '122480930', 'thanhdn05', '$2a$10$nM.jYx.3UtuaiZGb8RJLBuhLprOu2fJtrWAauSB.iFsLDvK2gWB7G', 0),
(7, 6, 'quynv', NULL, '2006-12-07', 'quynv@luvina.net', '122480931', 'quynv06', '$2a$10$hHncodr/6PItbwcyr4oZGucTpD4t6lvCgC.esfFgeiOjnApd3FNx.', 0),
(8, 7, 'lamhn', NULL, '2002-02-14', 'lamhn@luvina.net', '122480932', 'lamhn07', '$2a$10$nvnS66wn6z8X/6fehpkUc.TjpXo7GbGItWFwCHnj9KW2Dfh2eKWLq', 0),
(9, 8, 'minhpv', NULL, '2003-12-02', 'minhpv@luvina.net', '122480720', 'minhpv08', '$2a$10$X1GNoO9vDthgqB.ytXyPCuAZZlRGYxzBk/27T3Q93RzNDbdi0bt5i', 0),
(10, 4, 'hieuld', NULL, '2005-02-13', 'hieuld@luvina.net', '122480721', 'hieuld09', '$2a$10$Ye0maCHgZp56I16RLi7AEuYnLm74wSKReMGwUWiftXL7WxrHe/wDu', 0),
(11, 2, 'duytk', NULL, '2002-09-22', 'duytk@luvina.net', '122480722', 'duytk10', '$2a$10$vfr/Ryvi8P2xY5KF/cGQGOmifXqFUVK4yxkamHBYMNUNtMOWc03eW', 0),
(12, 1, 'khanhtd', NULL, '2002-10-10', 'khanhtd@luvina.net', '122480723', 'khanhtd11', '$2a$10$kwumq89nKTvslj7eohsuMukBFri7w8g1IzOChMfITtwOKusgone1u', 0),
(13, 4, 'hieunh', NULL, '2002-12-12', 'hieunh@luvina.net', '122480724', 'hieunh12', '$2a$10$rl26fqTBUUNZ0qlGqP5QxeghSLTwQj3BHIw7d1zhTZvBUes3T5fs6', 0),
(14, 2, 'minhdn', NULL, '2002-10-02', 'minhdn@luvina.net', '122480725', 'minhdn13', '$2a$10$vcCox6bcAAW3Nt7Qia8iHePkis4JLa0lrmijCaz3TlbRIeCE.bm7u', 0),
(15, 3, 'ducnv', NULL, '2002-06-22', 'ducnv@luvina.net', '122480726', 'ducnv14', '$2a$10$0x0cSW8eCHs./UktEbDaQOh7xSvZ1MpT.QyA6h7L0axRMBKN4ffv.', 0),
(16, 5, 'tudv', NULL, '2002-12-07', 'tudv@luvina.net', '122480727', 'tudv15', '$2a$10$c.JPI4tMMg9B0PSA2J9gh.SocB31ZljqtCTEUOOhfhm/ezhRmMNuS', 0),
(17, 6, 'tungnnh', NULL, '2002-02-02', 'tungnnh@luvina.net', '122480728', 'tungnnh16', '$2a$10$KE3rZr04mBUoEO0u43.jWe13Zq9Jmgalv7v4mTlfNMlj7e1Mz9ksa', 0),
(18, 7, 'hoangdn', NULL, '2003-03-20', 'hoangdn@luvina.net', '122480729', 'hoangdn17', '$2a$10$ATC1mVeK8MkWedUcOlXy/.uDWx4JtPXBE4xOpLu6amqqiqeRg9GB2', 0),
(19, 8, 'uyvq', NULL, '2002-12-02', 'uyvq@luvina.net', '122480730', 'uyvq18', '$2a$10$dnw2V93vhOLGy222FZkRfu56LNre90e6Sn6ysJtuCasyrtgKByT9G', 0),
(20, 1, 'ducdm', NULL, '2002-12-02', 'ducdm@luvina.net', '122480731', 'ducdm19', '$2a$10$jRXkBJFovG0MvoohlOJeZuyIgGR3.kNzU.TM5wmdtod0X6S1kDBVK', 0),
(21, 2, 'huannc', NULL, '2002-12-02', 'huannc@luvina.net', '122480732', 'huannc20', '$2a$10$jK5oEc6NeYyCpUlTz4eKLelW4tQMipgFaIRUuMD9koeWw1zhxF/NO', 0);

-- 5. Bảng employees_certifications (Chứng chỉ của nhân viên)
INSERT INTO `employees_certifications` (`employee_certification_id`, `employee_id`, `certification_id`, `start_date`, `end_date`, `score`) VALUES 
(1, 1, 1, '2026-05-01', '2028-05-01', 180.00),
(2, 2, 2, '2026-05-02', '2028-05-02', 150.00),
(3, 3, 3, '2026-05-03', '2028-05-03', 160.00),
(4, 4, 4, '2026-05-02', '2028-05-02', 180.00),
(5, 5, 5, '2026-05-03', '2028-05-03', 150.00),
(6, 6, 1, '2026-05-02', '2028-05-02', 160.00),
(7, 7, 2, '2026-05-03', '2028-05-03', 180.00),
(8, 8, 3, '2026-05-01', '2028-05-01', 150.00),
(9, 9, 3, '2026-05-02', '2028-05-02', 160.00),
(10, 10, 4, '2026-05-03', '2028-05-03', 180.00),
(11, 11, 5, '2026-05-01', '2028-05-01', 150.00),
(12, 12, 3, '2026-05-02', '2028-05-02', 160.00),
(13, 13, 4, '2026-05-03', '2028-05-03', 180.00),
(14, 14, 5, '2026-05-02', '2028-05-02', 150.00),
(15, 15, 3, '2026-05-03', '2028-05-03', 160.00),
(16, 16, 4, '2026-05-02', '2028-05-02', 180.00),
(17, 17, 3, '2026-05-03', '2028-05-03', 150.00),
(18, 18, 4, '2026-05-02', '2028-05-02', 160.00),
(19, 19, 5, '2026-05-03', '2028-05-03', 180.00),
(20, 20, 4, '2026-05-02', '2028-05-02', 150.00),
(21, 21, 5, '2026-05-03', '2028-05-03', 160.00);
