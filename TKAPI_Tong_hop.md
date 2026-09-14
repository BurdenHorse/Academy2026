# Tài liệu Thiết kế API & Database - Manager User

> **Hệ thống:** Manager User  
> **Người tạo / Update:** ThanhPD  
> **Ngày:** 2023-01-04  
> **Version:** 0.1  

Tài liệu tổng hợp từ các file TKAPI_*.xlsx và TKDB.xlsx, giữ nguyên nội dung thiết kế.

---

## 1. API Login

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | Thiết kế API |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Khái quát

#### 1. Khái quát

- Lấy thông tin chi tiết nhân viên
#### 2. Request

- Request URL
| No. | Service | API name | Method HTTP | Note |
|------|------|------|------|------|
| 1 | login | POST |
- Request Parameter
| No. | Parameter | Bắt buộc | Kiểu | Giá trị default | Tên hạng mục | Note |
|------|------|------|------|------|------|------|
| 1 | username | o | string | tên đăng nhập |
| 2 | password | o | string | mật khẩu đăng nhập |
```
※Sample
```
- /login
#### 3. Response

- Trường hợp api trả về response bình thường
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | accessToken | string |
| 2 | tokenType | string |
| 3 | errors | object |
```
※Sample
```
```
{
```
```
"accessToken": "200",
```
```
"tokenType": "1",
```
```
"errors": {}
```
```
}
```
- Trường hợp api trả về lỗi
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | accessToken | string | null |
| 2 | tokenType | string | null |
| 3 | errors | object | Thông tin lỗi |
```
※Sample
```
```
{
```
```
"accessToken": null,
```
```
"tokenType":null,
```
```
"errors": {
```
```
"code": "ER016"
```
```
}
```
```
}
```

---

## 2. API Get List Departments

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | Thiết kế API |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Khái quát

#### 1. Khái quát

- Lấy thông tin chi tiết người dùng
#### 2. Request

- Request URL
| No. | Service | API name | Method HTTP | Note |
|------|------|------|------|------|
| 1 | departments | GET |
- Request Parameter
| No. | Parameter | Bắt buộc | Kiểu | Giá trị default | Tên hạng mục | Note |
|------|------|------|------|------|------|------|
```
※Sample
```
- /departments
#### 3. Response

- Trường hợp api trả về response bình thường
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
- =ROW()-ROW($C$28) | departments | array | Mảng chứa thông tin list phòng ban
- =ROW()-ROW($C$28) | departmentId | number
- =ROW()-ROW($C$28) | departmentName | string
```
※Sample
```
```
{
```
```
"code": "200"
```
```
"departments": [
```
```
{
```
```
"departmentId": "1",
```
```
"departmentName": "Phòng DEV1",
```
```
},
```
```
{
```
```
"departmentId": "2",
```
```
"departmentName": "Phòng DEV2",
```
```
}
```
- ]
```
}
```
- Trường hợp api trả về lỗi
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
| 2 | message | object | Nội dung lỗi |
```
※Sample
```
```
{
```
```
"code": "500"
```
```
"message":  {
```
```
"code": "ER023"
```
```
"params": []
```
```
}
```
```
}
```

### Chi tiết xử lý

- Xử lý common:
- <Không có>
- Xử lý chi tiết:
#### 1. Get thông tin phòng ban

#### 1.1 Get tất cả phòng ban từ database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin bộ phận | departments | 〇 |
#### ■Table access

| ① | Hạng mục lấy |
- - Trường hợp get data hiển thị màn hình
- No | Tên bảng | Alias | Tên trường
| 1 | departments | - | department_id |
| 2 | departments | - | department_name |
#### 2. Tạo dữ liệu response cho API

- - Trường hợp không có lỗi xảy ra:
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 200 |
| 2 | departments | Lấy giá trị từ No 1 |
| 3 | departmentId |
| 4 | departmentName |
- - Trường hợp có lỗi xảy ra
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 500 |
| 2 | message | {code: "ER023", params: []} |
- - Kết thúc xử lý

---

## 3. API Get List Certifications

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | Thiết kế API |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Khái quát

#### 1. Khái quát

- Lấy thông tin danh sách chứng chỉ tiếng Nhật
#### 2. Request

- Request URL
| No. | Service | API name | Method HTTP | Note |
|------|------|------|------|------|
| 1 | certifications | GET |
- Request Parameter
| No. | Parameter | Bắt buộc | Kiểu | Giá trị default | Tên hạng mục | Note |
|------|------|------|------|------|------|------|
```
※Sample
```
- /certifications
#### 3. Response

- Trường hợp api trả về response bình thường
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
- =ROW()-ROW($C$28) | code | number
- =ROW()-ROW($C$28) | certifications | array | Mảng chứa thông tin chứng chỉ tiếng Nhật
- =ROW()-ROW($C$28) | certificationId | number
- =ROW()-ROW($C$28) | certificationName | string
```
※Sample
```
```
{
```
```
"code": "200"
```
```
"certifications": [
```
```
{
```
```
"certificationId": "1",
```
```
"certificationName": "Trình độ tiếng Nhật cấp 1",
```
```
},
```
```
{
```
```
"certificationId": "2",
```
```
"certificationName": "Trình độ tiếng Nhật cấp 2",
```
```
}
```
- ]
```
}
```
- Trường hợp api trả về lỗi
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
| 2 | message | object | Nội dung lỗi |
```
※Sample
```
```
{
```
```
"code": "500"
```
```
"message":  {
```
```
"code": "ER023"
```
```
"params": []
```
```
}
```
```
}
```

### Chi tiết xử lý

- Xử lý common:
- <Không có>
- Xử lý chi tiết:
#### 1. Get thông tin chứng chỉ tiếng Nhật

#### 1.1 Get tất cả chứng chỉ từ database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin chứng chỉ tiếng Nhật | certifications | 〇 |
#### ■Table access

| ① | Hạng mục lấy |
- - Trường hợp get data hiển thị màn hình
- No | Tên bảng | Alias | Tên trường
| 1 | certifications | - | certification_id |
| 2 | certifications | - | certification_name |
#### 2. Tạo dữ liệu response cho API

- - Trường hợp không có lỗi xảy ra:
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 200 |
| 2 | certifications | Lấy giá trị từ No 1 |
| 3 | certificationId |
| 4 | certificationName |
- - Trường hợp có lỗi xảy ra
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 500 |
| 2 | message | {code: "ER023", params: []} |
- - Kết thúc xử lý

---

## 4. API List Employees

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | Thiết kế API |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Khái quát

#### 1. Khái quát

- Lấy thông tin nhân viên và phòng ban cùng thông tin chứng chỉ tiếng Nhật (nếu có) của nhân viên theo điều kiện tìm kiếm để hiển thị màn hình list.
#### 2. Request

- Request URL
| No. | Service | API name | Method HTTP | Note |
|------|------|------|------|------|
| 1 | employee | GET |
- Request Parameter
| No. | Parameter | Bắt buộc | Kiểu | Giá trị default | Tên hạng mục | Note |
|------|------|------|------|------|------|------|
| 1 | employee_name | - | string | "" | Tên nhân viên | Tìm kiếm theo tên nhân viên |
| 2 | department_id | - | string | "" | ID phòng ban | Tìm kiếm theo tên phòng ban |
| 3 | ord_employee_name | - | string | "" | Tên hạng mục | = "ASC" hoặc "DESC" |
| 4 | ord_certification_name | - | string | "" | Tên chứng chỉ | = "ASC" hoặc "DESC" |
| 5 | ord_end_date | - | string | "" | Ngày kết thúc hiệu lực chứng chỉ | = "ASC" hoặc "DESC" |
| 6 | offset | - | string | "" | Vị trí bắt đầu get records | Không truyền vào sẽ lấy từ bản ghi đầu tiên (tức là offset = 0) |
| 7 | limit | - | string | "" | Giới hạn số records get | Không truyền vào sẽ lấy 5 |
```
※Sample
```
- /employee?employee_name=A&department_id=1&ord_employee_name=ASC&ord_certification_name=ASC&ord_end_date=DESC&offset=2&limit=30
#### 3. Response

- Trường hợp api trả về response bình thường
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
- =ROW()-ROW($C$37) | code | number
- =ROW()-ROW($C$37) | totalRecords | number | Tổng số nhân viên
- =ROW()-ROW($C$37) | employees | array | Mảng chứa thông tin nhân viên
- =ROW()-ROW($C$37) | employeeId | number
- =ROW()-ROW($C$37) | employeeName | string
- =ROW()-ROW($C$37) | employeeBirthDate | date
- =ROW()-ROW($C$37) | departmentName | string
- =ROW()-ROW($C$37) | employeeEmail | string
- =ROW()-ROW($C$37) | employeeTelephone | string
- =ROW()-ROW($C$37) | certificationName | string
- =ROW()-ROW($C$37) | endDate | date
- =ROW()-ROW($C$37) | score | decimal
```
※Sample
```
```
{
```
```
"code": "200"
```
```
"totalRecords": 2,
```
```
"employees": [
```
```
{
```
```
"employeeId": "1",
```
```
"employeeName": "Nguyễn Văn A",
```
```
"employeeBirthDate": "1983/01/01",
```
```
"departmentName": "Phòng DevN",
```
```
"employeeEmail": "nguyenvana@luvina.net",
```
```
"employeeTelephone": "01234567",
```
```
"certificationName": "Trình độ tiếng Nhật cấp 1",
```
```
"endDate": "9999/12/31",
```
```
"score": "999"
```
```
},
```
```
{
```
```
"employeeId": "2",
```
```
"employeeName": "Nguyễn Văn B",
```
```
"employeeBirthDate": "1983/01/02",
```
```
"departmentName": "Phòng DevN",
```
```
"employeeEmail": "nguyenvanb@luvina.net",
```
```
"employeeTelephone": "01234568",
```
```
"certificationName": "Trình độ tiếng Nhật cấp 2",
```
```
"endDate": "9999/12/31",
```
```
"score": "999"
```
```
}
```
- ]
```
}
```
- Trường hợp api trả về lỗi
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
| 2 | message | object | Nội dung lỗi |
```
※Sample
```
```
{
```
```
"code": "500"
```
```
"message":  {
```
```
"code": "ER015"
```
```
"params": []
```
```
}
```
```
}
```

### Chi tiết xử lý

- Xử lý common:
- <Không có>
- Xử lý chi tiết:
#### 1. Validate prameter

#### 1.1 Validate parameter [ord_employee_name], [ord_certification_name], [ord_end_date]

- Nếu giá trị order không phải là "ASC" hoặc DESC thì trả về lỗi có mã code ER021
#### 1.2 Validate parameter [offset]

- Nếu giá trị parameter này ko phải là số nguyên dương thì trả về lỗi có mã code ER018, tham số "オフセット"
#### 1.3 Validate parameter [limit]

- Nếu giá trị parameter này ko phải là số nguyên dương thì trả về lỗi có mã code ER018, tham số "リミット"
- Nếu có lỗi thì chuyển sang bước  [3. Tạo dữ liệu response cho API]
#### 2. Get danh sách nhân viên

#### 2.1 Thực hiện lấy tổng số nhân viên từ database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin nhân viên | employees | 〇 |
| 2 | Thông tin bộ phận | departments | 〇 |
#### ■Table access

| ① | Hạng mục lấy |
- - Trường hợp get data hiển thị màn hình
- No | Tên bảng | Alias | Tên trường
| 1 | employees | - | COUNT(employee_id) |
| ② | Điều kiện kết hợp |
- - Lấy thông tin phòng ban
- No | Tên bảng | Tên trường | Tên trường | Tên bảng / Alias | Điều kiện liên kết
| 1 | employees | department_id | department_id | departments | INNER JOIN |
| ③ | Điều kiện lấy |
- Nếu tồn tại parameter [department_id] và không rỗng => Thêm điều kiện search theo phòng ban
- No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình)
| 1 | employees | department_id | = parameter [department_id] |
- Nếu tồn tại parameter [employee_name] và không rỗng => Thêm điền kiện search theo tên nhân viên
- AND
- No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình)
| 1 | employees | employee_name | LIKE '%' + parameter [employee_name] + '%' |
- Nếu tổng số bản ghi là 0 thì chuyển đến  [3. Tạo dữ liệu response cho API]
#### 2.2 Thực hiện get danh sách nhân viên từ database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin nhân viên | employees | 〇 |
| 2 | Thông tin bộ phận | departments | 〇 |
| 3 | Thông tin chứng chỉ tiếng Nhật | certifications | 〇 |
| 4 | Thông tin nhân viên chứng chỉ tiếng Nhật | employees_certifications | 〇 |
#### ■Table access

| ① | Hạng mục lấy |
- - Trường hợp get data hiển thị màn hình
- No | Tên bảng | Alias | Tên trường
| 1 | employees | - | employee_id |
| 2 | employees | - | employee_name |
| 3 | employees | - | employee_birthdate |
| 4 | employees | - | employee_email |
| 5 | employees | - | employee_telephone |
| 7 | departments | - | department_name |
| 8 | certifications | - | certification_name |
| 9 | employees_certifications | - | end_date |
| 10 | employees_certifications | - | score |
| ② | Điều kiện kết hợp |
- - Lấy thông tin phòng ban
- No | Tên bảng | Tên trường | Tên trường | Tên bảng / Alias | Điều kiện liên kết
| 1 | employees | department_id | department_id | departments | INNER JOIN |
- - Lấy thông tin chứng chỉ tiếng Nhật
- No | Tên bảng | Tên trường | Tên trường | Tên bảng / Alias | Điều kiện liên kết
| 2 | employees | employee_id | employee_id | employees_certifications | LEFT JOIN |
| 3 | employees_certifications | certification_id | certification_id | certifications | LEFT JOIN |
| ③ | Điều kiện lấy |
- Nếu tồn tại parameter [department_id] và không rỗng => Thêm điều kiện search theo phòng ban
- AND
- No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình)
| 1 | employees | department_id | = parameter [department_id] |
- Nếu tồn tại parameter [employee_name] và không rỗng => Thêm điền kiện search theo tên nhân viên
- AND
- No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình)
| 1 | employees | employee_name | LIKE '%' + parameter [employee_name] + '%' |
| ④ | Sort |
- - Nếu tồn tại [ord_employee_name] và không rỗng  => Thêm sort theo hạng mục [氏名]
- No | Tên bảng | Tên hạng mục | Sort order
| 1 | employees | employee_name | parameter [ord_employee_name] (ASC hoặc DESC) |
- - Nếu tồn tại [ord_certification_name] và không rỗng  => Thêm sort theo hạng mục [日本語能力]
- No | Tên bảng | Tên hạng mục | Sort order
| 1 | certifications | certification_name | parameter [ord_certification_name] (ASC hoặc DESC) |
- - Nếu tồn tại [ord_end_date] và không rỗng  => Thêm sort theo hạng mục [失効日]
- No | Tên bảng | Tên hạng mục | Sort order
| 1 | employees_certifications | end_date | parameter [ord_end_date] (ASC hoặc DESC) |
- ※Nếu không chỉ định các tham số orderBy thì sort mặc định theo employee_id tăng dần
| ⑤ | Phân trang |
- No | Tên bảng | Tên hạng mục | Giá trị
| 1 | employees | limit | = parameter limit (nếu ko có giá trị thì để mặc định là 5) |
| 2 | employees | offset | = parameter offset (nếu ko có giá trị thì để mặc định là 0) |
#### 3. Tạo dữ liệu response cho API

- - Trường hợp không có lỗi xảy ra:
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 200 |
| 2 | totalRecords | Lấy giá trị từ No 2.1 |
| 3 | employees | Lấy giá trị từ No 2.2 |
| 4 | employeeId |
| 5 | employeeName |
| 6 | employeeBirthDate |
| 7 | departmentName |
| 8 | employeeEmail |
| 9 | employeeTelephone |
| 10 | certificationName |
| 11 | endDate |
| 12 | score |
- - Trường hợp có lỗi xảy ra
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 500 |
| 2 | message | Lấy giá trị từ No 1. Format {code: "", params: []} |
- - Kết thúc xử lý

---

## 5. API Get Employee

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | Thiết kế API |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Khái quát

#### 1. Khái quát

- Lấy thông tin chi tiết nhân viên
#### 2. Request

- Request URL
| No. | Service | API name | Method HTTP | Note |
|------|------|------|------|------|
| 1 | employee | GET |
- Request Parameter
| No. | Parameter | Bắt buộc | Kiểu | Giá trị default | Tên hạng mục | Note |
|------|------|------|------|------|------|------|
| 1 | employeeId | o | number | {} | id của employee cần lấy thông tin |
```
※Sample
```
- /employee/1
#### 3. Response

- Trường hợp api trả về response bình thường
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
- =ROW()-ROW($C$28) | employeeId | number
- =ROW()-ROW($C$28) | employeeName | string
- =ROW()-ROW($C$28) | employeeBirthDate | date
- =ROW()-ROW($C$28) | departmentId | string
- =ROW()-ROW($C$28) | departmentName | string
- =ROW()-ROW($C$28) | employeeEmail | string
- =ROW()-ROW($C$28) | employeeTelephone | string
- =ROW()-ROW($C$28) | employeeNameKana | string
- =ROW()-ROW($C$28) | employeeLoginId | string
- =ROW()-ROW($C$28) | certifications | array | Mảng chứa thông tin chứng chỉ tiếng Nhật
- =ROW()-ROW($C$28) | certificationId | number
- =ROW()-ROW($C$28) | certificationName | string
- =ROW()-ROW($C$28) | startDate | date
- =ROW()-ROW($C$28) | endDate | date
- =ROW()-ROW($C$28) | score | decimal
```
※Sample
```
```
{
```
```
"code": "200"
```
```
"employeeId": "1",
```
```
"employeeName": "Nguyễn Văn A",
```
```
"employeeBirthDate": "1983/01/02",
```
```
"departmentId": "1",
```
```
"departmentName": "Phòng DEVN",
```
```
"employeeEmail": "nguyenvana@luvina.net",
```
```
"employeeTelephone": "01234567",
```
```
"employeeNameKana": "名カナ",
```
```
"employeeLoginId": "nguyenvana",
```
```
"certifications": [
```
```
{
```
```
"certificationId": "1",
```
```
"certificationName": "chứng chỉ tiếng Nhật cấp 1",
```
```
"startDate": "2023/01/01",
```
```
"endDate": "2024/01/01",
```
```
"score": "180"
```
```
},
```
```
{
```
```
"certificationId": "2",
```
```
"certificationName": "chứng chỉ tiếng Nhật cấp 2",
```
```
"startDate": "2023/02/01",
```
```
"endDate": "2024/02/01",
```
```
"score": "90"
```
```
}
```
- ]
```
}
```
- Trường hợp api trả về lỗi
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
| 2 | message | object | Nội dung lỗi |
```
※Sample
```
```
{
```
```
"code": "500"
```
```
"message":  {
```
```
"code": "ER013"
```
```
"params": []
```
```
}
```
```
}
```

### Chi tiết xử lý

- Xử lý common:
- <Không có>
- Xử lý chi tiết:
#### 1. Validate prameter

#### 1.1 Validate parameter [employeeId]

- Nếu ko tồn tại parameter này thì trả về lỗi có mã code ER001, tham số "ＩＤ"
- Nếu không tồn tại trong bảng employees.employee_id thì trả về lỗi có mã code ER013, tham số "ＩＤ"
- Nếu có lỗi thì chuyển sang bước  [4. Tạo dữ liệu response cho API]
#### 2. Get thông tin chi tiết nhân viên

#### 2.1 Get nhân viên từ database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin nhân viên | employees | 〇 |
| 2 | Thông tin bộ phận | departments | 〇 |
#### ■Table access

| ① | Hạng mục lấy |
- - Trường hợp get data hiển thị màn hình
- No | Tên bảng | Alias | Tên trường
| 1 | employees | - | employee_id |
| 2 | employees | - | employee_name |
| 3 | employees | - | employee_birth_date |
| 4 | employees | - | employee_email |
| 5 | employees | - | employee_telephone |
| 6 | employees | - | employee_name_kana |
| 7 | employees | - | employee_login_id |
| 8 | departments | - | department_id |
| 8 | departments | - | department_name |
| ② | Điều kiện kết hợp |
- - Lấy thông tin phòng ban
- No | Tên bảng | Tên trường | Tên trường | Tên bảng / Alias | Điều kiện liên kết
| 1 | employees | department_id | department_id | departments | INNER JOIN |
#### 3. Get thông tin chứng chỉ tiếng Nhật của nhân viên

#### 3.1 Get danh sách chứng chỉ tiếng Nhật của nhân viên từ database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin chứng chỉ tiếng Nhật | certifications | 〇 |
| 2 | Thông tin nhân viên chứng chỉ tiếng Nhật | employees_certifications | 〇 |
#### ■Table access

| ① | Hạng mục lấy |
- - Trường hợp get data hiển thị màn hình
- No | Tên bảng | Alias | Tên trường
| 9 | certifications | - | certification_id |
| 10 | certifications | - | certification_name |
| 11 | employees_certifications | - | start_date |
| 12 | employees_certifications | - | end_date |
| 13 | employees_certifications | - | score |
| ② | Điều kiện kết hợp |
- - Lấy thông tin chứng chỉ tiếng Nhật
- No | Tên bảng | Tên trường | Tên trường | Tên bảng / Alias | Điều kiện liên kết
| 1 | employees_certifications | certification_id | certification_id | certifications | INNER JOIN |
| ③ | Sort |
- No | Tên bảng | Tên hạng mục | Sort order
| 1 | certifications | certification_level | ASC |
| ④ | WHERE |
- No | Tên bảng | Tên hạng mục | Giá trị
| 1 | employees_certifications | employee_id | = employee_id từ parameter [employeeId] |
#### 4. Tạo dữ liệu response cho API

- - Trường hợp không có lỗi xảy ra:
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 200 |
| 2 | employeeId | Lấy giá trị từ No 2 |
| 3 | employeeName |
| 4 | employeeBirthDate |
| 5 | departmentId |
| 6 | departmentName |
| 7 | employeeEmail |
| 8 | employeeTelephone |
| 9 | employeeNameKana |
| 10 | employeeLoginId |
| 11 | certifications | Lấy giá trị từ No 3 |
| 12 | certificationId |
| 13 | certificationName |
| 14 | startDate |
| 15 | endDate |
| 16 | score |
- - Trường hợp có lỗi xảy ra
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 500 |
| 2 | message | Lấy giá trị từ No 1 . Format {code: "", params: []} |
- - Kết thúc xử lý

---

## 6. API Add Employee

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | Thiết kế API |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Khái quát

#### 1. Khái quát

- Tạo mới nhân viên
#### 2. Request

- Request URL
| No. | Service | API name | Method HTTP | Note |
|------|------|------|------|------|
| 1 | employee | POST |
- Request Parameter
| No. | Parameter | Bắt buộc | Kiểu | Giá trị default | Tên hạng mục | Note |
|------|------|------|------|------|------|------|
| 1 | key | string | {} | Tên hạng mục | Lưu vào table employees |
- value | string | {} | giá trị hạng mục | Lưu vào table employees
| 2 | certifications | - | arrar | {} | Mảng lưu các chứng chỉ tiếng Nhật của nhân viên |
- key | string | {} | Tên hạng mục | Lưu vào table employees_certifications
- value | string | {} | giá trị hạng mục | Lưu vào table employees_certifications
```
※Sample
```
```
{
```
```
"employeeName": "Nguyễn Văn A",
```
```
"employeeBirthDate": "1983/01/01",
```
```
"employeeEmail": "nguyenvana@luvina.net",
```
```
"employeeTelephone": "01234567",
```
```
"employeeNameKana": "01234567",
```
```
"employeeLoginId": "01234567",
```
```
"employeeLoginPassword": "01234567",
```
```
"departmentId": "1",
```
```
"certifications": [
```
```
{
```
```
"certificationId": "1",
```
```
"certificationStartDate": "2023/01/01",
```
```
"certificationEndDate": "2024/01/01",
```
```
"employeeCertificationScore": "999"
```
```
},
```
```
{
```
```
"certificationId": "2",
```
```
"certificationStartDate": "2023/06/01",
```
```
"certificationEndDate": "2024/06/01",
```
```
"employeeCertificationScore": "999"
```
```
}
```
- ]
```
}
```
#### 3. Response

- Trường hợp api trả về response bình thường
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
| 2 | employeeId | number |
| 3 | message | object |
```
※Sample
```
```
{
```
```
"code": "200"
```
```
"employeeId": "1",
```
```
"message":  {
```
```
"code": "MSG001"
```
```
"params": []
```
```
}
```
```
}
```
- Trường hợp api trả về lỗi
```
※Sample
```
```
{
```
```
"code": "500"
```
```
"message":  {
```
```
"code": "ER015"
```
```
"params": []
```
```
}
```
```
}
```

### Chi tiết xử lý

- Xử lý common:
- <Không có>
- Xử lý chi tiết:
#### 1. Validate parameter

#### 1.1 Validate parameter [employeeLoginId]

- Nếu ko tồn tại parameter này thì trả về lỗi có mã code ER001, tham số "アカウント名"
- Nếu có độ dài vượt quá 50 ký tự thì trả về lỗi có mã code ER006, tham số "アカウント名"
- Nếu không thỏa mãn điều kiện chỉ chứa các ký tự a-z, A-Z, 0-9 và _ hoặc ký tự đầu tiên là số thì trả về lỗi có mã code ER019
- Nếu đã tồn tại trong bảng employees.employee_login_id thì trả về lỗi có mã code ER003, tham số "アカウント名"
#### 1.2 Validate parameter [employeeName]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "氏名"
- Nếu có độ dài vượt quá 125 ký tự thì trả về lỗi có mã code ER006, tham số "氏名"
#### 1.3 Validate parameter [employeeNameKana]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "カタカナ氏名"
- Nếu có độ dài vượt quá 125 ký tự thì trả về lỗi có mã code ER006, tham số "カタカナ氏名"
- Nếu không phải là chỉ chứa ký tự katakana thì trả về lỗi có mã code ER009, tham số "カタカナ氏名"
#### 1.4 Validate parameter [employeeBirthDate]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "生年月日"
- Nếu không phải giá trị ngày tháng hợp lệ thì trả về lỗi có mã code ER011, tham số "生年月日"
- Nếu không thỏa mãn định dạng yyyy/MM/dd thì trả về lỗi có mã code ER005, tham số "生年月日", "yyyy/MM/dd"
#### 1.5 Validate parameter [employeeEmail]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "メールアドレス"
- Nếu có độ dài vượt quá 125 ký tự thì trả về lỗi có mã code ER006, tham số "メールアドレス"
#### 1.6 Validate parameter [employeeTelephone]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "電話番号"
- Nếu có độ dài vượt quá 50 ký tự thì trả về lỗi có mã code ER006, tham số "電話番号"
- Nếu chứa ký tự ngoài ký tự 1 byte thì trả về lỗi có mã code ER008, tham số "電話番号"
#### 1.7 Validate parameter [employeeLoginPassword]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "パスワード"
- Nếu có độ dài vượt quá 50 ký tự hoặc ngắn hơn 8 ký tự thì trả về lỗi có mã code ER007, tham số "パスワード" , 8, 50
#### 1.8 Validate parameter [departmentId]

- Nếu ko tồn tại parameter này thì trả về lỗi có mã code ER002, tham số "グループ"
- Nếu giá trị parameter này ko phải là số nguyên dương thì trả về lỗi có mã code ER018, tham số "グループ"
- Nếu giá trị của parameter này ko tồn tại trong departments.departmentId thì trả về lỗi có mã code ER004, tham số "グループ"
#### 1.9 Validate parameter [certifications]

- Nếu request có truyền parameter này thì cần check chi tiết bên dưới
- startDate | Nếu không tồn tại hoặc bị rỗng | =>  trả về lỗi có mã code ER001, tham số "資格交付日"
- Nếu không phải giá trị ngày tháng hợp lệ | =>  trả về lỗi có mã code ER001, tham số "資格交付日"
- Nếu không thỏa mãn định dạng yyyy/MM/dd | =>  trả về lỗi có mã code ER005, tham số "資格交付日", "yyyy/MM/dd"
- endDate | Nếu không tồn tại hoặc bị rỗng | =>  trả về lỗi có mã code ER001, tham số "失効日"
- Nếu không phải giá trị ngày tháng hợp lệ | =>  trả về lỗi có mã code ER001, tham số "失効日"
- Nếu không thỏa mãn định dạng yyyy/MM/dd | =>  trả về lỗi có mã code ER005, tham số "失効日", "yyyy/MM/dd"
- Nếu certificationEndDate < certificationStartDate | =>  trả về lỗi có mã code ER012
- score | Nếu không tồn tại hoặc bị rỗng | =>  trả về lỗi có mã code ER001, tham số "点数"
- Nếu không phải là giá trị kiểu số nguyên dương | =>  trả về lỗi có mã code ER018, tham số "点数"
- certificationId | Nếu không tồn tại hoặc bị rỗng | =>  trả về lỗi có mã code ER001, tham số "資格"
- Nếu không phải giá trị kiểu số nguyên dương | =>  trả về lỗi có mã code ER018, tham số "資格"
- Nếu không tồn tại giá trị này trong  certifications.certification_id | =>  trả về lỗi có mã code ER004, tham số "資格"
- Nếu có lỗi thì chuyển sang bước  [4. Tạo dữ liệu response cho API]
- Khởi tạo transaction
#### 2. Insert thông tin nhân viên

#### 2.1 Insert nhân viên vào database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin nhân viên | employees | 〇 |
#### ■Table access

| ① | Hạng mục insert |
- No | Tên bảng | Alias | Tên trường | Giá trị
| 1 | employees | - | employee_id | Ko cần insert vì để tự động tăng |
| 2 | employees | - | department_id | parameter [departmentId] |
| 3 | employees | - | employee_name | parameter [employeeName] |
| 4 | employees | - | employee_name_kana | parameter [employeeNameKana] |
| 5 | employees | - | employee_birth_date | parameter [employeeBirthDate] |
| 7 | employees | - | employee_email | parameter [employeeEmail] |
| 8 | employees | - | employee_telephone | parameter [employeeTelephone] |
| 9 | employees | - | employee_login_id | parameter [employeeLoginId] |
| 10 | employees | - | employee_login_password | parameter [employeeLoginPassword] |
#### 3. Nếu tồn tại parameter [certifications] thì thực hiện Insert chứng chỉ tiếng Nhật

#### 3.1 Insert chứng chỉ vào database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin nhân viên chứng chỉ tiếng Nhật | employees_certifications | 〇 |
#### ■Table access

| ① | Hạng mục insert |
- No | Tên bảng | Alias | Tên trường | Giá trị
| 1 | employees_certifications | - | employee_certification_id | Ko cần insert vì để tự động tăng |
| 2 | employees_certifications | - | employee_id | lấy từ bước 2 |
| 3 | employees_certifications | - | certification_id | parameter [certificationId] |
| 4 | employees_certifications | - | start_date | parameter [startDate] |
| 5 | employees_certifications | - | end_date | parameter [endDate] |
| 7 | employees_certifications | - | score | parameter [score] |
- Nếu không có lỗi gì xảy ra thì Commit transaction
- Nếu có lỗi xảy ra thì Rollback transaction, di chuyển đến [4. Tạo dữ liệu response cho API] với mã lỗi ER015
#### 4. Tạo dữ liệu response cho API

- - Trường hợp không có lỗi xảy ra:
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 200 |
| 2 | employeeId | Lấy giá trị từ No 2 |
| 3 | message | {code: "MSG001", params: []} |
- - Trường hợp có lỗi xảy ra
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 500 |
| 2 | message | Lấy giá trị từ No 1, No 2, No 3. Format {code: "", params: []} |
- - Kết thúc xử lý

---

## 7. API Update Employee

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | Thiết kế API |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Khái quát

#### 1. Khái quát

- Update nhân viên
#### 2. Request

- Request URL
| No. | Service | API name | Method HTTP | Note |
|------|------|------|------|------|
| 1 | employee | PUT |
- Request Parameter
| No. | Parameter | Bắt buộc | Kiểu | Giá trị default | Tên hạng mục | Note |
|------|------|------|------|------|------|------|
| 1 | key | string | {} | Tên hạng mục | Lưu vào table employees |
- value | string | {} | giá trị hạng mục | Lưu vào table employees
| 2 | certifications | - | object | {} | Đối tượng lưu chứng chỉ tiếng Nhật |
- key | string | {} | Tên hạng mục | Lưu vào table employees_certifications
- value | string | {} | giá trị hạng mục | Lưu vào table employees_certifications
```
※Sample
```
```
{
```
```
"employeeId": "1",
```
```
"employeeName": "Nguyễn Văn A",
```
```
"employeeBirthDate": "1983/01/01",
```
```
"employeeEmail": "nguyenvana@luvina.net",
```
```
"employeeTelephone": "01234567",
```
```
"employeeNameKana": "01234567",
```
```
"employeeLoginId": "01234567",
```
```
"employeeLoginPassword": "01234567",
```
```
"departmentId": "1",
```
```
"certifications":
```
```
{
```
```
"certificationId": "1",
```
```
"startDate": "2023/01/01",
```
```
"endDate": "2024/01/01",
```
```
"score": "999"
```
```
}
```
```
}
```
#### 3. Response

- Trường hợp api trả về response bình thường
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
| 2 | employeeId | number |
| 3 | message | object |
```
※Sample
```
```
{
```
```
"code": "200",
```
```
"employeeId": "1",
```
```
"message":  {
```
```
"code": "MSG002"
```
```
"params": []
```
```
}
```
```
}
```
- Trường hợp api trả về lỗi
```
※Sample
```
```
{
```
```
"code": "500",
```
```
"message":  {
```
```
"code": "ER015"
```
```
"params": []
```
```
}
```
```
}
```

### Chi tiết xử lý

- Xử lý common:
- <Không có>
- Xử lý chi tiết:
#### 1. Validate parameter

#### 1.1 Validate parameter [employeeId]

- Nếu ko tồn tại parameter này thì trả về lỗi có mã code ER001, tham số "ＩＤ"
- Nếu không tồn tại trong bảng employees.employee_id thì trả về lỗi có mã code ER013, tham số "ＩＤ"
#### 1.2 Validate parameter [employeeLoginId]

- Nếu ko tồn tại parameter này thì trả về lỗi có mã code ER001, tham số "アカウント名"
- Nếu có độ dài vượt quá 50 ký tự thì trả về lỗi có mã code ER006, tham số "アカウント名"
- Nếu không thỏa mãn điều kiện chỉ chứa các ký tự a-z, A-Z, 0-9 và _ hoặc ký tự đầu tiên là số thì trả về lỗi có mã code ER019
- Nếu trùng với giá trị KHÁC trong bảng employees.employee_login_id thì trả về lỗi có mã code ER003, tham số "アカウント名"
#### 1.3 Validate parameter [employeeName]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "氏名"
- Nếu có độ dài vượt quá 125 ký tự thì trả về lỗi có mã code ER006, tham số "氏名"
#### 1.4 Validate parameter [employeeNameKana]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "カタカナ氏名"
- Nếu có độ dài vượt quá 125 ký tự thì trả về lỗi có mã code ER006, tham số "カタカナ氏名"
- Nếu không phải là chỉ chứa ký tự katakana thì trả về lỗi có mã code ER009, tham số "カタカナ氏名"
#### 1.5 Validate parameter [employeeBirthDate]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "生年月日"
- Nếu không phải giá trị ngày tháng hợp lệ thì trả về lỗi có mã code ER011, tham số "生年月日"
- Nếu không thỏa mãn định dạng yyyy/MM/dd thì trả về lỗi có mã code ER005, tham số "生年月日", "yyyy/MM/dd"
#### 1.6 Validate parameter [employeeEmail]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "メールアドレス"
- Nếu có độ dài vượt quá 125 ký tự thì trả về lỗi có mã code ER006, tham số "メールアドレス"
#### 1.7 Validate parameter [employeeTelephone]

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "電話番号"
- Nếu có độ dài vượt quá 50 ký tự thì trả về lỗi có mã code ER006, tham số "電話番号"
- Nếu chứa ký tự ngoài ký tự 1 byte thì trả về lỗi có mã code ER008, tham số "電話番号"
#### 1.8 Nếu parameter [employeeLoginPassword] khác rỗng thì thực hiện validate

- Nếu ko tồn tại parameter này hoặc giá trị parameter là rỗng thì trả về lỗi có mã code ER001, tham số "パスワード"
- Nếu có độ dài vượt quá 50 ký tự hoặc ngắn hơn 8 ký tự thì trả về lỗi có mã code ER007, tham số "パスワード" , 8, 50
#### 1.9 Validate parameter [departmentId]

- Nếu ko tồn tại parameter này thì trả về lỗi có mã code ER002, tham số "グループ"
- Nếu giá trị parameter này ko phải là số nguyên dương thì trả về lỗi có mã code ER018, tham số "グループ"
- Nếu giá trị của parameter này ko tồn tại trong departments.departmentId thì trả về lỗi có mã code ER004, tham số "グループ"
#### 1.10 Validate parameter [certifications]

- Nếu request có truyền parameter này thì cần check chi tiết bên dưới
- startDate | Nếu không tồn tại hoặc bị rỗng | =>  trả về lỗi có mã code ER001, tham số "資格交付日"
- Nếu không phải giá trị ngày tháng hợp lệ | =>  trả về lỗi có mã code ER001, tham số "資格交付日"
- Nếu không thỏa mãn định dạng yyyy/MM/dd | =>  trả về lỗi có mã code ER005, tham số "資格交付日", "yyyy/MM/dd"
- endDate | Nếu không tồn tại hoặc bị rỗng | =>  trả về lỗi có mã code ER001, tham số "失効日"
- Nếu không phải giá trị ngày tháng hợp lệ | =>  trả về lỗi có mã code ER001, tham số "失効日"
- Nếu không thỏa mãn định dạng yyyy/MM/dd | =>  trả về lỗi có mã code ER005, tham số "失効日", "yyyy/MM/dd"
- Nếu endDate < startDate | =>  trả về lỗi có mã code ER012
- score | Nếu không tồn tại hoặc bị rỗng | =>  trả về lỗi có mã code ER001, tham số "点数"
- Nếu không phải là giá trị kiểu số nguyên dương | =>  trả về lỗi có mã code ER018, tham số "点数"
- certificationId | Nếu không tồn tại hoặc bị rỗng | =>  trả về lỗi có mã code ER001, tham số "資格"
- Nếu không phải giá trị kiểu số nguyên dương | =>  trả về lỗi có mã code ER018, tham số "資格"
- Nếu không tồn tại giá trị này trong  certifications.certification_id | =>  trả về lỗi có mã code ER004, tham số "資格"
- Nếu có lỗi thì chuyển sang bước  [4. Tạo dữ liệu response cho API]
- Khởi tạo transaction
#### 2. Update thông tin nhân viên

#### 2.1 Update nhân viên vào database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin nhân viên | employees | 〇 |
#### ■Table access

| ① | Hạng mục update |
- No | Tên bảng | Alias | Tên trường | Giá trị
| 1 | employees | - | department_id | parameter [departmentId] |
| 2 | employees | - | employee_name | parameter [employeeName] |
| 3 | employees | - | employee_name_kana | parameter [employeeNameKana] |
| 4 | employees | - | employee_birthdate | parameter [employeeBirthDate] |
| 5 | employees | - | employee_email | parameter [employeeEmail] |
| 6 | employees | - | employee_telephone | parameter [employeeTelephone] |
| 7 | employees | - | employee_login_id | parameter [employeeLoginId] |
| 8 | employees | - | employee_login_password | parameter [employeeLoginPassword] |
- ＊Nếu tham số là rỗng thì không update hạng mục này
| ② | Điều kiện update |
- No | Tên bảng | Tên trường | Điều kiện (giá trị, tên hạng mục màn hình)
| 1 | employees | employee_id | =parameter [employeeId] |
#### 3. Update chứng chỉ tiếng Nhật

#### 3.1 Xóa thông tin chứng chỉ hiện có của nhân viên

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin chứng chỉ tiếng Nhật của nhân viên | employees_certifications | 〇 |
#### ■Table access

| ① | Điều kiện xóa |
- No | Tên bảng | Tên hạng mục | Giá trị
| 1 | employees_certifications | employee_id | = employee_id từ parameter [employeeId] |
#### 3.2 Nếu tồn tại thông tin parameter [certifications] thì thực hiện insert thông tin chứng chỉ vào database

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin nhân viên chứng chỉ tiếng Nhật | employees_certifications | 〇 |
#### ■Table access

| ① | Hạng mục update |
- No | Tên bảng | Alias | Tên trường | Giá trị
| 1 | employees_certifications | - | employee_certification_id | Ko cần insert vì để tự động tăng |
| 2 | employees_certifications | - | employee_id | parameter [employeeId] |
| 3 | employees_certifications | - | certification_id | parameter [certificationId] |
| 4 | employees_certifications | - | start_date | parameter [startDate] |
| 5 | employees_certifications | - | end_date | parameter [endDate] |
| 7 | employees_certifications | - | score | parameter [score] |
- Nếu không có lỗi gì xảy ra thì Commit transaction
- Nếu có lỗi xảy ra thì Rollback transaction, di chuyển đến [4. Tạo dữ liệu response cho API] với mã lỗi ER015
#### 4. Tạo dữ liệu response cho API

- - Trường hợp không có lỗi xảy ra:
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 200 |
| 2 | employeeId | Lấy giá trị từ parameter [employeeId] |
| 3 | message | {code: "MSG002", params: []} |
- - Trường hợp có lỗi xảy ra
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 500 |
| 2 | message | Lấy giá trị từ No 1, No 2, No 3. Format {code: "", params: []} |
- - Kết thúc xử lý

---

## 8. API Delete Employee

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | Thiết kế API |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Khái quát

#### 1. Khái quát

- Lấy thông tin chi tiết nhân viên
#### 2. Request

- Request URL
| No. | Service | API name | Method HTTP | Note |
|------|------|------|------|------|
| 1 | employee | DELETE |
- Request Parameter
| No. | Parameter | Bắt buộc | Kiểu | Giá trị default | Tên hạng mục | Note |
|------|------|------|------|------|------|------|
| 1 | employeeId | o | number | {} | id của employee cần xóa |
```
※Sample
```
- /employee/1
#### 3. Response

- Trường hợp api trả về response bình thường
| No. | json key name | Kiểu | Tên hạng mục | Note |
|------|------|------|------|------|
| 1 | code | number |
- =ROW()-ROW($C$28) | employeeId | number
- =ROW()-ROW($C$28) | message | object
```
※Sample
```
```
{
```
```
"code": "200"
```
```
"employeeId": "1",
```
```
"message":  {
```
```
"code": "MSG003"
```
```
"params": []
```
```
}
```
```
}
```
- Trường hợp api trả về lỗi
```
※Sample
```
```
{
```
```
"code": "500"
```
```
"employeeId": "1",
```
```
"message":  {
```
```
"code": "ER015"
```
```
"params": []
```
```
}
```
```
}
```

### Chi tiết xử lý

- Xử lý common:
- <Không có>
- Xử lý chi tiết:
#### 1. Validate prameter

#### 1.1 Validate parameter [employeeId]

- Nếu ko tồn tại parameter này thì trả về lỗi có mã code ER001, tham số "ＩＤ"
- Nếu không tồn tại trong bảng employees.employee_id thì trả về lỗi có mã code ER014, tham số "ＩＤ"
- Nếu có lỗi thì chuyển sang bước  [4. Tạo dữ liệu response cho API]
- Khởi tạo transaction
#### 2. Xóa thông tin trình độ tiếng Nhật của nhân viên

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin chứng chỉ tiếng Nhật của nhân viên | employees_certifications | 〇 |
#### ■Table access

| ① | Điều kiện xóa |
- No | Tên bảng | Tên hạng mục | Giá trị
| 1 | employees_certifications | employee_id | = employee_id từ parameter [employeeId] |
- Nếu có lỗi khi xóa thì trả về lỗi và chuyển sang bước  [4. Tạo dữ liệu response cho API]
#### 3. Xóa thông tin nhân viên

#### ■Danh sách bảng sử dụng

- No | Tên bảng logic | ID bảng vật lý | Create | Refer | Update | Xóa
| 1 | Thông tin nhân viên | employees | 〇 |
#### ■Table access

| ① | Điều kiện xóa |
- No | Tên bảng | Tên hạng mục | Giá trị
| 1 | employees | employee_id | = employee_id từ parameter [employeeId] |
- Nếu không có lỗi gì xảy ra thì Commit transaction
- Nếu có lỗi xảy ra thì Rollback transaction
- Nếu có lỗi khi xóa thì trả về lỗi với mã lỗi ER015 và chuyển sang bước  [4. Tạo dữ liệu response cho API]
#### 4. Tạo dữ liệu response cho API

- - Trường hợp không có lỗi xảy ra:
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 200 |
| 2 | employeeId | Lấy giá trị từ parameter [employeeId] |
| 3 | message | {code: "MSG003", params: []} |
- - Trường hợp có lỗi xảy ra
| No. | Key | Giá trị | Note |
|------|------|------|------|
| 1 | code | 500 |
| 2 | employeeId | Lấy giá trị từ parameter [employeeId] |
| 3 | message | Lấy giá trị từ No 1, No 2, No 3. Format {code: "", params: []} |
- - Kết thúc xử lý

---

## 9. Thiết kế Database (TKDB)

### Thông tin chung

| Hạng mục | Giá trị |
|----------|---------|
| Tên system | Manager User |
| Loại | 14_Thiết kế DB |
| Người tạo | ThanhPD |
| Ngày tạo | 2023-01-04 |
| Version | 0.1 |

### Danh sách bảng

- Danh sách bảng
- ●Danh sách các bảng
**No. | Tên logic | Tên vật lý**

| 1 | Bảng thông tin nhân viên | employees |
- =B11+1 | Bảng thông tin phòng ban | departments
- =B12+1 | Bảng thông tin trình độ tiếng Nhật | certifications
- =B13+1 | Bảng quan hệ nhân viên trình độ tiếng Nhật | employees_certifications

### employees

- =H11
#### ■Thông tin bảng

**Tên schema | 社員サービス | Tên RDBMS | Aurora**

**Tên logic | Bảng thông tin nhân viên | Mysql**

- Tên vật lý | employees
- Ghi chú
#### ■Thông tin cột

**No. | Tên logic | Tên vật lý | Data Type | PK | Not Null | Format | Ghi chú**

| 1 | ID nhân viên | employee_id | BIGINT | Yes | Yes | Auto increment |
| 2 | ID bộ phân | department_id | BIGINT | Yes | FK departments.department_id |
| 3 | Tên (Tên) | employee_name | VARCHAR(255) | Yes |
| 4 | Tên (kana) (Tên) | employee_name_kana | VARCHAR(255) |
| 5 | Ngày sinh | employee_birth_date | DATE |
| 6 | Địa chỉ email | employee_email | VARCHAR(255) | Yes |
| 7 | Số điện thoại | employee_telephone | VARCHAR(50) |
| 8 | Tên tài khoản | employee_login_id | VARCHAR(50) | Yes |
| 9 | Mật khẩu | employee_login_password | VARCHAR(100) |

### departments

- =H11
#### ■Thông tin bảng

**Tên schema | user | Tên RDBMS | Aurora**

**Tên logic | Bảng thông tin phòng ban | Mysql**

- Tên vật lý | departments
- Ghi chú
#### ■Thông tin cột

**No. | Tên logic | Tên vật lý | Data Type | PK | Not Null | Format | Ghi chú**

| 1 | ID bộ phân | department_id | BIGINT | Yes | Yes | Auto increment |
| 2 | Tên bộ phận | department_name | VARCHAR(50) | Yes |

### certifications

- - | =H11
#### ■Thông tin bảng

**Tên schema | user | Tên RDBMS | Aurora**

**Tên logic | Bảng thông tin trình độ tiếng Nhật | Mysql**

- Tên vật lý | certifications
- Ghi chú
#### ■Thông tin cột

**No. | Tên logic | Tên vật lý | Data Type | PK | Not Null | Format | Ghi chú**

| 1 | ID chứng chỉ tiếng Nhật | certification_id | BIGINT | Yes | Yes | Auto increment |
| 2 | Tên chứng chỉ | certification_name | VARCHAR(50) | Yes |
| 3 | Cấp độ của chứng chỉ | certification_level | INT | Yes | Giá trị càng nhỏ thì trình độ càng cao |
#### ■Data Master

- certification_id | certification_name | certification_level
| 1 | Trình độ tiếng nhật cấp 1 | 1 |
| 2 | Trình độ tiếng nhật cấp 2 | 2 |
| 3 | Trình độ tiếng nhật cấp 3 | 3 |
| 4 | Trình độ tiếng nhật cấp 4 | 4 |
| 5 | Trình độ tiếng nhật cấp 5 | 5 |

### employees_certifications

- =H11
#### ■Thông tin bảng

**Tên schema | user | Tên RDBMS | Aurora**

**Tên logic | Bảng quan hệ nhân viên trình độ tiếng Nhật | Mysql**

- Tên vật lý | employees_certifications
- Ghi chú
#### ■Thông tin cột

**No. | Tên logic | Tên vật lý | Data Type | PK | Not Null | Format | Ghi chú**

| 1 | ID quan hệ nhân viên và chứng chỉ tiếng Nhật | employee_certification_id | BIGINT | Yes | Yes | Auto increment |
- =B23+1 | ID nhân viên | employee_id | BIGINT | Yes | Yes | FK employees.employee_id
- =B24+1 | ID chứng chỉ | certification_id | BIGINT | Yes | Yes | FK certifications.certification_id
- =B25+1 | Ngày đạt chứng chỉ | start_date | DATE | Yes
- =B26+1 | Người hết hạn chứng chỉ | end_date | DATE | Yes
- =B27+1 | Điểm | score | DECIMAL | Yes

### Sơ đồ ER

#### Sơ đồ ER


---

*Tổng hợp đầy đủ nội dung từ các file thiết kế API và Database gốc.*