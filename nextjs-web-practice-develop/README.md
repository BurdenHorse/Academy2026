# Employee Management System - Frontend (Next.js)

Giao diện Web Client cho hệ thống Quản lý nhân viên (**Employee Management System**), được xây dựng trên nền tảng Next.js App Router kết hợp TypeScript, tuân thủ chặt chẽ đặc tả thiết kế màn hình (TKMH) cho các màn hình ADM001 đến ADM005.

---

## 1. Công nghệ sử dụng

- **Framework**: Next.js 16 (App Router) + React 19
- **Ngôn ngữ**: TypeScript
- **Styling**: CSS Modules / Vanilla CSS (giao diện pixel-perfect, chuẩn theo mockup thiết kế)
- **Quản lý Form & Validation**: React Hook Form, Zod kết hợp custom validators chuẩn tiếng Nhật (Hankaku Katakana, ngày tháng, định dạng số)
- **HTTP Client**: Axios (cấu hình interceptor tự động đính kèm token, xử lý refresh, xử lý lỗi hệ thống)
- **Kiểm thử tự động**: Jest + React Testing Library (93/93 tests passed trên 15 test suites)

---

## 2. Yêu cầu môi trường & Cài đặt

### 2.1. Yêu cầu
- **Node.js**: Phiên bản 18.x hoặc 20.x trở lên
- **npm** (hoặc `yarn` / `pnpm`)
- **Backend API**: Khởi chạy sẵn tại `http://localhost:8085`

### 2.2. Cài đặt các thư viện phụ thuộc
```bash
cd nextjs-web-practice-develop
npm install
```

### 2.3. Cấu hình Biến môi trường
Tạo tệp `.env.local` tại thư mục gốc của frontend:
```env
NEXT_PUBLIC_API_URL=http://localhost:8085
```

---

## 3. Khởi chạy dự án

### 3.1. Chạy ở môi trường Development
```bash
npm run dev
```
Truy cập ứng dụng tại trình duyệt: **`http://localhost:3000`**

### 3.2. Build & Chạy môi trường Production
```bash
npm run build
npm start
```

### 3.3. Chạy kiểm thử tự động (Unit / Component Tests)
```bash
npm test
```
*(Bao phủ 15 test suites, 93 test cases cho các components, hooks, auth và validation schemas)*.

---

## 4. Các màn hình chức năng đã hoàn thành

| Mã màn hình | Tên màn hình | Đường dẫn | Chức năng chính |
| :--- | :--- | :--- | :--- |
| **ADM001** | Đăng nhập | `/login` | Xác thực người dùng, lưu JWT Token vào session, chuyển hướng về danh sách nhân viên |
| **ADM002** | Danh sách nhân viên | `/employees` | Tìm kiếm theo tên & phòng ban; Sắp xếp theo 3 tiêu chí (tên, chứng chỉ, ngày hết hạn); Phân trang (5 bản ghi/trang); Modal xác nhận xóa nhân viên |
| **ADM003** | Thêm mới nhân viên | `/employees/add` | Form nhập thông tin nhân viên và trình độ tiếng Nhật; Kiểm tra dữ liệu real-time; Lưu thông tin thành công |
| **ADM004** | Chỉnh sửa nhân viên | `/employees/edit/[id]` | Tải dữ liệu nhân viên theo ID; Cho phép chỉnh sửa thông tin & đổi mật khẩu (tùy chọn); Cập nhật vào hệ thống |
| **ADM005** | Chi tiết nhân viên | `/employees/[id]` | Xem chi tiết thông tin nhân viên và chứng chỉ; Điều hướng sang màn hình chỉnh sửa hoặc quay lại |
| **ERR001** | Lỗi hệ thống | `/system-error` | Hiển thị thông báo khi gặp lỗi 500 hoặc sự cố kết nối máy chủ |

---

## 5. Cấu trúc thư mục mã nguồn

```text
nextjs-web-practice-develop/
├── app/                     # Next.js App Router (Routing & Layouts)
│   ├── (auth)/login/        # Màn hình đăng nhập ADM001
│   ├── (auth)/logout/       # Xử lý đăng xuất và xóa phiên
│   ├── employees/           # Màn hình danh sách ADM002
│   │   ├── add/             # Màn hình thêm mới ADM003
│   │   ├── edit/[id]/       # Màn hình chỉnh sửa ADM004
│   │   └── [id]/            # Màn hình chi tiết ADM005
│   └── system-error/        # Màn hình lỗi hệ thống
├── components/              # React Components
│   ├── common/              # Components dùng chung (Header, Footer, Pagination, Modal, Input,...)
│   └── employees/           # Components chức năng (adm002.tsx, adm003.tsx, adm004.tsx,...)
├── constants/               # Hệ thống hằng số tập trung (Clean Code)
│   ├── endpoints.ts         # API URLs
│   ├── codes.ts             # Mã lỗi nghiệp vụ (ER001..ER021), HTTP Status Codes
│   ├── messages.ts          # Định dạng câu thông báo tiếng Nhật chuẩn
│   ├── labels.ts            # Nhãn UI, tiêu đề cột, placeholders
│   ├── routes.ts            # Đường dẫn điều hướng nội bộ
│   └── index.ts             # Export tổng hợp (limits, regex, sort keys, storage keys)
├── hooks/                   # Custom React Hooks
│   ├── useADM002.ts         # Quản lý state tìm kiếm, sort 3 cột, phân trang
│   ├── useADM003.ts         # Quản lý form thêm mới nhân viên
│   ├── useADM004.ts         # Quản lý form cập nhật nhân viên
│   ├── useDepartments.ts    # Fetch và cache danh sách phòng ban
│   └── useCertifications.ts # Fetch và cache danh sách trình độ tiếng Nhật
├── lib/
│   ├── api/                 # Axios client, interceptors & API callers
│   ├── auth/                # Quản lý token xác thực
│   └── validation/          # Bộ schemas xác thực Zod & quy tắc định dạng tiếng Nhật
├── styles/                  # Định nghĩa biến CSS, Reset CSS và stylesheet toàn cục
└── __tests__/               # Thư mục kiểm thử tự động với Jest & Testing Library
```

---

## 6. Các điểm nổi bật về mặt kỹ thuật

1. **Chuẩn hóa Constants 100%**:
   - Toàn bộ URL, key lưu trữ, thông điệp tiếng Nhật, nhãn màn hình, giới hạn độ dài ký tự đều được quản lý tập trung trong thư mục `constants/`, không tồn tại magic string/number trong code.
2. **Kiểm soát tính hợp lệ đa tầng (Form Validation)**:
   - Validate trực tiếp tại client bằng Zod và React Hook Form, đồng bộ 100% với danh sách mã lỗi của tài liệu thiết kế (ER001, ER002, ER003, ER005, ER007, ER008, ER009, ER011, ER012, ER018, ER021).
   - Hỗ trợ kiểm tra ký tự Halfsize Katakana, định dạng số nửa độ rộng và ngày hợp lệ (`yyyy/MM/dd`).
3. **Trải nghiệm người dùng (UX)**:
   - Phân trang chuẩn với vùng hiển thị liên mạch từ bảng dữ liệu.
   - Sắp xếp đa tiêu chí ưu tiên trực quan với icon mũi tên đảo chiều.
   - Trạng thái loading và modal xác nhận rõ ràng, an toàn trước khi thực hiện hành động xóa.
