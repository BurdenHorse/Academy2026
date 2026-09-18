# Employee Management System - Backend (Spring Boot)

Hệ thống Backend cung cấp dịch vụ RESTful API cho ứng dụng Quản lý nhân viên (**Employee Management System**), phục vụ bài tập cuối khóa Luvina Academy.

---

## 1. Công nghệ sử dụng

- **Ngôn ngữ**: Java 17
- **Framework**: Spring Boot 2.7.4
- **Security**: Spring Security + JSON Web Token (JWT)
- **Persistence / ORM**: Spring Data JPA, Hibernate, Native SQL tùy biến
- **Database**: MySQL 8.0, Flyway Migration
- **Mapping**: MapStruct (Java annotation-based mapper)
- **Tiện ích**: Lombok, Slf4j, BCrypt
- **Kiểm thử tự động**: JUnit 5, Mockito (36/36 tests passed)
- **Công cụ build**: Apache Maven

---

## 2. Yêu cầu môi trường & Cài đặt

### 2.1. Yêu cầu
- **JDK**: Java Development Kit 17 (đã cấu hình biến môi trường `JAVA_HOME`).
- **MySQL**: MySQL Server 8.0 (khởi chạy trên cổng mặc định 3306).
- **Maven**: Phiên bản 3.6+ (hoặc dùng trực tiếp wrapper `./mvnw` có sẵn trong dự án).

### 2.2. Cấu hình Cơ sở dữ liệu
1. Tạo database trống trên MySQL:
   ```sql
   CREATE DATABASE IF NOT EXISTS user_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Cập nhật thông tin kết nối trong `src/main/resources/config/application-dev.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/user_manager?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
       username: root
       password: your_password
   ```
3. **Flyway Migration**: Khi khởi động ứng dụng, Flyway sẽ tự động chạy các tệp migration trong `src/main/resources/db/migration/` (`V1` đến `V4`) để tạo bảng và seed dữ liệu mẫu kiểm thử.

---

## 3. Khởi chạy dự án

### 3.1. Chạy ở môi trường Development
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / MacOS
./mvnw spring-boot:run
```
Mặc định dịch vụ sẽ chạy tại: **`http://localhost:8085`**

### 3.2. Chạy ở môi trường Production
```bash
./mvnw -Pprod spring-boot:run
```

### 3.3. Chạy kiểm thử tự động (Unit / Integration Tests)
```bash
mvn test
```
*(Hiện tại hệ thống bao phủ đầy đủ 36/36 test cases cho `EmployeeValidatorTest` và `EmployeeServiceTest`)*.

---

## 4. Danh sách RESTful APIs

| Phương thức | Đường dẫn (Endpoint) | Mô tả | Yêu cầu Authentication |
| :--- | :--- | :--- | :---: |
| `POST` | `/login` | Đăng nhập hệ thống, trả về JWT Access Token | ❌ Không |
| `GET` | `/departments` | Lấy danh sách phòng ban (sắp xếp `department_id` tăng dần) | ✅ Bearer Token |
| `GET` | `/certifications` | Lấy danh sách trình độ tiếng Nhật (sắp xếp Level tăng dần) | ✅ Bearer Token |
| `GET` | `/employee` | Tìm kiếm & phân trang danh sách nhân viên (hỗ trợ filter, sort 3 cột) | ✅ Bearer Token |
| `GET` | `/employee/{id}` | Lấy thông tin chi tiết một nhân viên | ✅ Bearer Token |
| `POST` | `/employee` | Thêm mới nhân viên và chứng chỉ | ✅ Bearer Token |
| `PUT` | `/employee` | Cập nhật thông tin nhân viên và chứng chỉ | ✅ Bearer Token |
| `DELETE` | `/employee/{id}` | Xóa nhân viên theo ID | ✅ Bearer Token |

### Định dạng Header xác thực:
Với các API yêu cầu xác thực, truyền kèm JWT trong Header HTTP:
```http
Authorization: Bearer <access_token>
```

---

## 5. Cấu trúc mã nguồn

```text
backend-web-practice-develop/
├── src/main/java/com/luvina/la/
│   ├── config/              # Cấu hình Spring Security, CORS, JWT Filter, Constants tập trung
│   │   ├── jwt/             # JwtTokenProvider, JwtTokenFilter, JwtAuthenticationEntryPoint
│   │   └── Constants.java   # Định nghĩa toàn bộ hằng số hệ thống, mã lỗi, regex, limits
│   ├── controller/          # REST Controller tiếp nhận request (Employee, Department, Certification, Auth)
│   ├── dto/                 # Data Transfer Objects trao đổi giữa các tầng
│   ├── entity/              # JPA Entities (EmployeeEntity, DepartmentEntity, CertificationEntity,...)
│   ├── exception/           # AppException & GlobalExceptionHandler xử lý lỗi tập trung
│   ├── mapper/              # MapStruct interfaces tự động ánh xạ Entity <-> DTO
│   ├── payload/
│   │   ├── request/         # Request body DTOs (EmployeeCreateRequest, Update,...)
│   │   └── response/        # Response payload chuẩn (ListEmployeeResponse, ErrorResponse,...)
│   ├── repository/          # Spring Data JPA Repositories & Custom Native SQL Repository
│   ├── service/             # Service interfaces & Service implementations (Business logic)
│   ├── util/                # Tiện ích bổ trợ (DateTimeUtil, StringUtil)
│   └── validator/           # Bộ kiểm tra nghiệp vụ và định dạng (EmployeeValidator)
└── src/main/resources/
    ├── config/              # application.yaml, application-dev.yaml, application-prod.yaml
    ├── db/migration/        # Flyway SQL migrations (DDL & Initial Seed Data)
    └── logback-spring.xml   # Cấu hình logging
```

---

## 6. Các điểm nổi bật về mặt kiến trúc & kỹ thuật

1. **Clean Architecture & Phân tách rõ ràng**:
   - Controller chỉ làm nhiệm vụ nhận request và trả response.
   - Toàn bộ nghiệp vụ kiểm tra tính hợp lệ (format, độ dài, logic ngày tháng, tiếng Nhật Hankaku Katakana) được module hóa tại `EmployeeValidator`.
   - Toàn bộ chuyển đổi dữ liệu được ủy quyền cho `MapStruct` mapper.
2. **Xử lý Native SQL hiệu năng cao**:
   - Tầng repository (`EmployeeRepositoryCustomImpl`) sử dụng truy vấn Native SQL thuần kết hợp các hàm tối ưu của MySQL (`DATE_FORMAT`, `COUNT`).
   - Sắp xếp linh hoạt đa cột (`sortPriority`), hỗ trợ chống SQL Wildcard Injection với mệnh đề `ESCAPE '\\'`.
3. **Quản lý hằng số tập trung**:
   - Loại bỏ hoàn toàn magic strings/numbers bằng lớp `Constants.java`.
4. **Bảo mật**:
   - Mã hóa mật khẩu một chiều chuẩn `BCrypt`.
   - Phân quyền người dùng và kiểm soát phiên làm việc phi trạng thái (Stateless) bằng `JwtTokenFilter`.
