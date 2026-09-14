# Đặc tả thiết kế API — Manager User

> Hệ thống: **Manager User** · Loại: Thiết kế API · Version: 0.1  
> Tổng hợp từ bộ tài liệu thiết kế gốc — dùng để tham chiếu khi implement backend.

Mỗi API gồm 3 phần: **Khái quát**, **Flow xử lý**, **Chi tiết xử lý** (theo đúng tài liệu).

## Mục lục

1. [Get List certifications](#3-get-list-certifications)
2. [List employees](#4-list-employees)

---
## 1. Get List Departments

**Endpoint:** `GET` `/department`

### Khái quát

Lấy thông tin chi tiết người dùng

### Flow xử lý

1. Get thông tin phòng ban
2. Tạo dữ liệu response cho API

### Chi tiết xử lý

**Xử lý common:**

<Không có>

**Xử lý chi tiết:**

#### 1. Get thông tin phòng ban

##### 1.1 Get tất cả phòng ban từ database

**■Danh sách bảng sử dụng**

| No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Thông tin bộ phận | departments | 〇 |  |  |  |

**■Table access**

- ① | Hạng mục lấy
- Trường hợp get data hiển thị màn hình

| No | Tên bảng | Alias | Tên trường |
| --- | --- | --- | --- |
| 1 | departments | - | department_id |
| 2 | departments | - | department_name |

#### 2. Tạo dữ liệu response cho API

**- Trường hợp không có lỗi xảy ra:**

| No. | Key | Giá trị | Note |
| --- | --- | --- | --- |
| 1 | code | 200 |  |
| 2 | departments | Lấy giá trị từ No 1 |  |
| 3 | departmentId |  |  |
| 4 | departmentName |  |  |

- Trường hợp có lỗi xảy ra

| No. | Key | Giá trị | Note |
| --- | --- | --- | --- |
| 1 | code | 500 |  |
| 2 | message | {code: "ER023", params: []} |  |

- Kết thúc xử lý

---

## 2. List employees

**Endpoint:** `GET` `/employee?employee_name=A&department_id=1&ord_employee_name=ASC&ord_certification_name=ASC&ord_end_date=DESC&offset=2&limit=30`

### Khái quát

Lấy thông tin nhân viên và phòng ban cùng thông tin chứng chỉ tiếng Nhật (nếu có) của nhân viên theo điều kiện tìm kiếm để hiển thị màn hình list.

### Flow xử lý

1. Validate prameter
2. Get danh sách nhân viên
3. Tạo dữ liệu response cho API

### Chi tiết xử lý

**Xử lý common:**

<Không có>

**Xử lý chi tiết:**

#### 1. Validate prameter

##### 1.1 Validate parameter [ord_employee_name], [ord_certification_name], [ord_end_date]

Nếu giá trị order không phải là "ASC" hoặc DESC thì trả về lỗi có mã code ER021

##### 1.2 Validate parameter [offset]

Nếu giá trị parameter này ko phải là số nguyên dương thì trả về lỗi có mã code ER018, tham số "オフセット"

##### 1.3 Validate parameter [limit]

Nếu giá trị parameter này ko phải là số nguyên dương thì trả về lỗi có mã code ER018, tham số "リミット"

Nếu có lỗi thì chuyển sang bước  [3. Tạo dữ liệu response cho API]

#### 2. Get danh sách nhân viên

##### 2.1 Thực hiện lấy tổng số nhân viên từ database

**■Danh sách bảng sử dụng**

| No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Thông tin nhân viên | employees | 〇 |  |  |  |
| 2 | Thông tin bộ phận | departments | 〇 |  |  |  |

**■Table access**

- ① | Hạng mục lấy
- Trường hợp get data hiển thị màn hình

| No | Tên bảng | Alias | Tên trường |
| --- | --- | --- | --- |
| 1 | employees | - | COUNT(employee_id) |

- ② | Điều kiện kết hợp
- Lấy thông tin phòng ban

| No | Tên bảng | Tên trường | Tên trường | Tên bảng / Alias | Điều kiện liên kết |
| --- | --- | --- | --- | --- | --- |
| 1 | employees | department_id | department_id | departments | INNER JOIN |

- ③ | Điều kiện lấy
Nếu tồn tại parameter [department_id] và không rỗng => Thêm điều kiện search theo phòng ban

| No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình) |
| --- | --- | --- | --- |
| 1 | employees | department_id | = parameter [department_id] |

Nếu tồn tại parameter [employee_name] và không rỗng => Thêm điền kiện search theo tên nhân viên

AND

| No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình) |
| --- | --- | --- | --- |
| 1 | employees | employee_name | LIKE '%' + parameter [employee_name] + '%' |

Nếu tổng số bản ghi là 0 thì chuyển đến  [3. Tạo dữ liệu response cho API]

##### 2.2 Thực hiện get danh sách nhân viên từ database

**■Danh sách bảng sử dụng**

| No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Thông tin nhân viên | employees | 〇 |  |  |  |
| 2 | Thông tin bộ phận | departments | 〇 |  |  |  |
| 3 | Thông tin chứng chỉ tiếng Nhật | certifications | 〇 |  |  |  |
| 4 | Thông tin nhân viên chứng chỉ tiếng Nhật | employees_certifications | 〇 |  |  |  |

**■Table access**

- ① | Hạng mục lấy
- Trường hợp get data hiển thị màn hình

| No | Tên bảng | Alias | Tên trường |
| --- | --- | --- | --- |
| 1 | employees | - | employee_id |
| 2 | employees | - | employee_name |
| 3 | employees | - | employee_birthdate |
| 4 | employees | - | employee_email |
| 5 | employees | - | employee_telephone |
| 7 | departments | - | department_name |
| 8 | certifications | - | certification_name |
| 9 | employees_certifications | - | end_date |
| 10 | employees_certifications | - | score |

- ② | Điều kiện kết hợp
- Lấy thông tin phòng ban

| No | Tên bảng | Tên trường | Tên trường | Tên bảng / Alias | Điều kiện liên kết |
| --- | --- | --- | --- | --- | --- |
| 1 | employees | department_id | department_id | departments | INNER JOIN |

- Lấy thông tin chứng chỉ tiếng Nhật

| No | Tên bảng | Tên trường | Tên trường | Tên bảng / Alias | Điều kiện liên kết |
| --- | --- | --- | --- | --- | --- |
| 2 | employees | employee_id | employee_id | employees_certifications | LEFT JOIN |

- 3 | employees_certifications | certification_id | certification_id | certifications | LEFT JOIN
- ③ | Điều kiện lấy
Nếu tồn tại parameter [department_id] và không rỗng => Thêm điều kiện search theo phòng ban

AND

| No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình) |
| --- | --- | --- | --- |
| 1 | employees | department_id | = parameter [department_id] |

Nếu tồn tại parameter [employee_name] và không rỗng => Thêm điền kiện search theo tên nhân viên

AND

| No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình) |
| --- | --- | --- | --- |
| 1 | employees | employee_name | LIKE '%' + parameter [employee_name] + '%' |

- ④ | Sort
- Nếu tồn tại [ord_employee_name] và không rỗng  => Thêm sort theo hạng mục [氏名]

| No | Tên bảng | Tên hạng mục | Sort order |
| --- | --- | --- | --- |
| 1 | employees | employee_name | parameter [ord_employee_name] (ASC hoặc DESC) |

- Nếu tồn tại [ord_certification_name] và không rỗng  => Thêm sort theo hạng mục [日本語能力]

| No | Tên bảng | Tên hạng mục | Sort order |
| --- | --- | --- | --- |
| 1 | certifications | certification_name | parameter [ord_certification_name] (ASC hoặc DESC) |

- Nếu tồn tại [ord_end_date] và không rỗng  => Thêm sort theo hạng mục [失効日]

| No | Tên bảng | Tên hạng mục | Sort order |
| --- | --- | --- | --- |
| 1 | employees_certifications | end_date | parameter [ord_end_date] (ASC hoặc DESC) |

※Nếu không chỉ định các tham số orderBy thì sort mặc định theo employee_id tăng dần

- ⑤ | Phân trang
| No | Tên bảng | Tên hạng mục | Giá trị |
| --- | --- | --- | --- |
| 1 | employees | limit | = parameter limit (nếu ko có giá trị thì để mặc định là 5) |
| 2 | employees | offset | = parameter offset (nếu ko có giá trị thì để mặc định là 0) |

#### 3. Tạo dữ liệu response cho API

**- Trường hợp không có lỗi xảy ra:**

| No. | Key | Giá trị | Note |
| --- | --- | --- | --- |
| 1 | code | 200 |  |
| 2 | totalRecords | Lấy giá trị từ No 2.1 |  |
| 3 | employees | Lấy giá trị từ No 2.2 |  |
| 4 | employeeId |  |  |
| 5 | employeeName |  |  |
| 6 | employeeBirthDate |  |  |
| 7 | departmentName |  |  |
| 8 | employeeEmail |  |  |
| 9 | employeeTelephone |  |  |
| 10 | certificationName |  |  |
| 11 | endDate |  |  |
| 12 | score |  |  |

- Trường hợp có lỗi xảy ra

| No. | Key | Giá trị | Note |
| --- | --- | --- | --- |
| 1 | code | 500 |  |
| 2 | message | Lấy giá trị từ No 1. Format {code: "", params: []} |  |

- Kết thúc xử lý