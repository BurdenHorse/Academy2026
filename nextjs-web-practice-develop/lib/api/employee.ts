import { apiClient } from './client';
import { 
  EmployeeListResponse, 
  EmployeeSearchParams,
  AddEmployeePayload,
  AddEmployeeResponse,
  UpdateEmployeePayload,
  UpdateEmployeeResponse,
  EmployeeDetailResponse,
  DeleteEmployeeResponse
} from '@/types/employee';
import { API_ENDPOINTS } from '@/constants';

/**
 * Module cung cấp các hàm gọi API liên quan đến quản lý nhân viên.
 */
export const employeeApi = {
  /**
   * Lấy danh sách nhân viên theo điều kiện tìm kiếm, sắp xếp đa cột và phân trang.
   * Endpoint: GET /employee
   *
   * @param params Các tham số tìm kiếm
   * @returns Promise<EmployeeListResponse> Chứa danh sách nhân viên và tổng số bản ghi
   */
  getEmployees: async (params: EmployeeSearchParams): Promise<EmployeeListResponse> => {
    const queryParams: Record<string, string | number> = {};
    
    if (params.employee_name) queryParams.employee_name = params.employee_name;
    if (params.department_id) queryParams.department_id = params.department_id;
    if (params.ord_employee_name) queryParams.ord_employee_name = params.ord_employee_name;
    if (params.ord_certification_name) queryParams.ord_certification_name = params.ord_certification_name;
    if (params.ord_end_date) queryParams.ord_end_date = params.ord_end_date;
    if (params.sort_priority) queryParams.sort_priority = params.sort_priority;
    if (params.offset !== undefined) queryParams.offset = params.offset;
    if (params.limit !== undefined) queryParams.limit = params.limit;

    const response = await apiClient.get<EmployeeListResponse>(API_ENDPOINTS.EMPLOYEE, {
      params: queryParams,
    });
    return response.data;
  },

  /**
   * Tạo mới một nhân viên kèm theo thông tin chứng chỉ (nếu có).
   * Endpoint: POST /employee
   *
   * @param payload Dữ liệu nhân viên cần thêm mới
   * @returns Promise<AddEmployeeResponse> Kết quả tạo mới và ID nhân viên
   */
  addEmployee: async (payload: AddEmployeePayload): Promise<AddEmployeeResponse> => {
    const response = await apiClient.post<AddEmployeeResponse>(API_ENDPOINTS.EMPLOYEE, payload);
    return response.data;
  },

  /**
   * Cập nhật thông tin nhân viên kèm theo thông tin chứng chỉ (nếu có).
   * Endpoint: PUT /employee
   *
   * @param payload Dữ liệu nhân viên cần cập nhật
   * @returns Promise<UpdateEmployeeResponse> Kết quả cập nhật và ID nhân viên
   */
  updateEmployee: async (payload: UpdateEmployeePayload): Promise<UpdateEmployeeResponse> => {
    const response = await apiClient.put<UpdateEmployeeResponse>(API_ENDPOINTS.EMPLOYEE, payload);
    return response.data;
  },

  /**
   * Lấy chi tiết thông tin nhân viên theo ID.
   * Endpoint: GET /employee/{id}
   *
   * @param employeeId ID nhân viên cần lấy chi tiết
   * @returns Promise<EmployeeDetailResponse> Thông tin chi tiết nhân viên
   */
  getEmployeeDetail: async (employeeId: string | number): Promise<EmployeeDetailResponse> => {
    const response = await apiClient.get<EmployeeDetailResponse>(`${API_ENDPOINTS.EMPLOYEE}/${employeeId}`);
    return response.data;
  },

  /**
   * Xóa nhân viên theo ID.
   * Endpoint: DELETE /employee/{id}
   *
   * @param employeeId ID nhân viên cần xóa
   * @returns Promise<DeleteEmployeeResponse> Kết quả xóa nhân viên
   */
  deleteEmployee: async (employeeId: string | number): Promise<DeleteEmployeeResponse> => {
    const response = await apiClient.delete<DeleteEmployeeResponse>(`${API_ENDPOINTS.EMPLOYEE}/${employeeId}`);
    return response.data;
  },
};
