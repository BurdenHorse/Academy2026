package com.luvina.la.repository;
/*
 * Copyright(C) 2026 Luvina Software Company
 *
 * EmployeeRepositoryCustomImpl.java, Aug 22, 2026 nvquy
 */

import com.luvina.la.config.Constants;
import com.luvina.la.dto.EmployeeListDTO;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Triển khai custom repository cho nhân viên sử dụng Native SQL thuần.
 * Tận dụng các hàm MySQL (DATE_FORMAT, COUNT) và JOIN trực tiếp các bảng vật lý,
 * hỗ trợ chống wildcard injection cho LIKE, sắp xếp đa cột theo thứ tự ưu tiên
 * và phân trang theo offset/limit theo đúng specification ADM002 & Thiết kế API.
 *
 * <p>Logic truy vấn DB:
 * employees e INNER JOIN departments d ON e.department_id = d.department_id
 * LEFT JOIN employees_certifications ec ON e.employee_id = ec.employee_id
 * LEFT JOIN certifications c ON ec.certification_id = c.certification_id</p>
 *
 * @author quynv
 */
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

    private static final String FROM_EMPLOYEES_JOIN =
            "FROM employees e "
            + "INNER JOIN departments d ON e.department_id = d.department_id "
            + "LEFT JOIN employees_certifications ec ON e.employee_id = ec.employee_id "
            + "LEFT JOIN certifications c ON ec.certification_id = c.certification_id ";

    private static final Map<String, String> SORT_COLUMN_MAP;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(Constants.PARAM_ORD_EMPLOYEE_NAME, "e.employee_name");
        map.put(Constants.PARAM_ORD_CERTIFICATION_NAME, "c.certification_name");
        map.put(Constants.PARAM_ORD_END_DATE, "ec.end_date");
        SORT_COLUMN_MAP = Collections.unmodifiableMap(map);
    }

    @PersistenceContext
    private EntityManager entityManager;

    private final EmployeeMapper employeeMapper;

    public EmployeeRepositoryCustomImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Thực hiện truy vấn Native SQL với các bước:
     * 1. Xây dựng câu lệnh SELECT với DATE_FORMAT và các bảng JOIN
     * 2. Thêm điều kiện WHERE (escape LIKE wildcard chống injection và lọc department_id)
     * 3. Thêm ORDER BY theo thứ tự ưu tiên các cột
     * 4. Thêm LIMIT và OFFSET
     * 5. Map kết quả Object[] sang EmployeeListDTO</p>
     */
    @Override
    public List<EmployeeListDTO> searchEmployees(
            String employeeName,
            Long departmentId,
            String ordEmployeeName,
            String ordCertificationName,
            String ordEndDate,
            String sortPriority,
            int offset,
            int limit) {
        // Xây dựng câu truy vấn Native SQL động
        // Tạo câu SELECT cơ bản
        StringBuilder sqlBuilder = buildSelectQuery();
        // Thêm điều kiện WHERE tuỳ chỉnh dựa trên các tham số employeeName, departmentId
        appendWhereClause(sqlBuilder, employeeName, departmentId);
        // Thêm Điều kiện OrderBy để thực hiện sort theo 3 hạng mục
        appendOrderByClause(sqlBuilder, ordEmployeeName, ordCertificationName, ordEndDate, sortPriority);
        // Thực hiện phân trang
        sqlBuilder.append("LIMIT :limit OFFSET :offset ");

        // Chuyển sang query
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        setQueryParameters(query, employeeName, departmentId);
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);

        // Map kết quả từ Object[] sang EmployeeListDTO qua EmployeeMapper
        @SuppressWarnings("unchecked")
        List<Object[]> queryResults = query.getResultList();
        return employeeMapper.toEmployeeListDTOList(queryResults);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Sử dụng cùng cấu trúc JOIN và WHERE với searchEmployees
     * nhưng chỉ đếm tổng số bản ghi bằng COUNT() của Native SQL.</p>
     */
    @Override
    public long countEmployees(String employeeName, Long departmentId) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT COUNT(e.employee_id) ");
        sqlBuilder.append(FROM_EMPLOYEES_JOIN);
        appendWhereClause(sqlBuilder, employeeName, departmentId);

        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        setQueryParameters(query, employeeName, departmentId);

        Number countResult = (Number) query.getSingleResult();
        return countResult != null ? countResult.longValue() : 0L;
    }

    /**
     * Xây dựng phần SELECT và FROM của câu truy vấn Native SQL.
     * Tận dụng DATE_FORMAT để format ngày tháng trực tiếp thành chuỗi yyyy/MM/dd.
     *
     * @return StringBuilder chứa phần SELECT và FROM của câu truy vấn
     */
    private StringBuilder buildSelectQuery() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("e.employee_id, ");
        sqlBuilder.append("e.employee_name, ");
        sqlBuilder.append("DATE_FORMAT(e.employee_birth_date, '").append(Constants.SQL_DATE_FORMAT).append("') AS employee_birth_date, ");
        sqlBuilder.append("d.department_name, ");
        sqlBuilder.append("e.employee_email, ");
        sqlBuilder.append("e.employee_telephone, ");
        sqlBuilder.append("c.certification_name, ");
        sqlBuilder.append("DATE_FORMAT(ec.end_date, '").append(Constants.SQL_DATE_FORMAT).append("') AS end_date, ");
        sqlBuilder.append("ec.score ");
        sqlBuilder.append(FROM_EMPLOYEES_JOIN);
        return sqlBuilder;
    }

    /**
     * Thêm điều kiện WHERE vào câu truy vấn Native SQL theo điều kiện tìm kiếm.
     * - Chỉ hiển thị nhân viên có role là USER (role = 0), không bao giờ hiển thị ADMIN (role = 1)
     * - employee_name: tìm gần đúng LIKE %...% với ESCAPE '\\' để chống wildcard injection
     * - department_id: lọc chính xác theo phòng ban
     *
     * @param sql          StringBuilder chứa câu truy vấn đang xây dựng
     * @param employeeName Tên nhân viên cần tìm (có thể null)
     * @param departmentId ID phòng ban cần lọc (có thể null)
     */
    private void appendWhereClause(StringBuilder sql, String employeeName, Long departmentId) {
        sql.append("WHERE e.role = :roleUser ");
        if (employeeName != null) {
            sql.append("AND e.employee_name LIKE :employeeName ESCAPE '\\\\' ");
        }
        if (departmentId != null) {
            sql.append("AND d.department_id = :departmentId ");
        }
    }

    /**
     * Thêm điều kiện ORDER BY vào câu truy vấn Native SQL.
     * Hỗ trợ sắp xếp đồng thời theo nhiều cột vật lý theo thứ tự ưu tiên:
     * - employee_name (tên nhân viên)
     * - certification_name (tên chứng chỉ)
     * - end_date (ngày hết hạn chứng chỉ)
     * Nếu không có điều kiện sort nào, mặc định sắp xếp theo employee_id ASC.
     *
     * @param sql                  StringBuilder chứa câu truy vấn đang xây dựng
     * @param ordEmployeeName      Thứ tự sắp xếp theo tên (ASC/DESC), null nếu không sort
     * @param ordCertificationName Thứ tự sắp xếp theo chứng chỉ (ASC/DESC), null nếu không sort
     * @param ordEndDate           Thứ tự sắp xếp theo ngày hết hạn (ASC/DESC), null nếu không sort
     * @param sortPriority         Thứ tự ưu tiên các cột sort truyền từ client
     */
    private void appendOrderByClause(StringBuilder sql, String ordEmployeeName,
                                      String ordCertificationName, String ordEndDate,
                                      String sortPriority) {
        // Map tên param -> direction (ASC/DESC)

        // Nếu không truyền sort param → mặc định ASC theo thứ tự ưu tiên ban đầu
        Map<String, String> sortDirectionMap = new HashMap<>();
        sortDirectionMap.put(Constants.PARAM_ORD_EMPLOYEE_NAME, ordEmployeeName != null ? ordEmployeeName : Constants.SORT_ASC);
        sortDirectionMap.put(Constants.PARAM_ORD_CERTIFICATION_NAME, ordCertificationName != null ? ordCertificationName : Constants.SORT_ASC);
        sortDirectionMap.put(Constants.PARAM_ORD_END_DATE, ordEndDate != null ? ordEndDate : Constants.SORT_ASC);

        // Có truyền sort param
        // Xác định thứ tự ưu tiên: dùng sortPriority nếu có, không thì dùng thứ tự mặc định
        List<String> priorityOrder;
        if (!StringUtil.isNullOrEmpty(sortPriority)) {
            priorityOrder = StringUtil.splitAndTrim(sortPriority, Constants.DELIMITER_COMMA).stream()
                .filter(SORT_COLUMN_MAP::containsKey)
                .collect(Collectors.toList());
            // Thêm các cột còn thiếu vào cuối (giữ thứ tự mặc định)
            for (String key : SORT_COLUMN_MAP.keySet()) {
                if (!priorityOrder.contains(key)) {
                    priorityOrder.add(key);
                }
            }
        } else {
            priorityOrder = new ArrayList<>(SORT_COLUMN_MAP.keySet());
        }

        // Xây dựng ORDER BY theo thứ tự ưu tiên
        List<String> orderClauses = new ArrayList<>();
        for (String key : priorityOrder) {
            String direction = sortDirectionMap.get(key);
            if (direction != null) {
                orderClauses.add(SORT_COLUMN_MAP.get(key) + " " + direction);
            }
        }

        // Mặc định sắp xếp theo employee_id tăng dần nếu không có điều kiện sort
        if (orderClauses.isEmpty()) {
            orderClauses.add("e.employee_id ASC");
        }

        sql.append("ORDER BY ").append(String.join(", ", orderClauses)).append(" ");
    }

    /**
     * Gán giá trị tham số cho câu truy vấn Native SQL.
     * Thực hiện escape các ký tự đặc biệt của LIKE (%, _, \) trước khi gán tham số.
     *
     * @param query        Query cần gán tham số
     * @param employeeName Tên nhân viên (có thể null — không gán nếu null)
     * @param departmentId ID phòng ban (có thể null — không gán nếu null)
     */
    private void setQueryParameters(Query query, String employeeName, Long departmentId) {
        query.setParameter("roleUser", Constants.ROLE_USER);
        if (employeeName != null) {
            String escapedName = StringUtil.escapeLikeWildcards(employeeName);
            query.setParameter("employeeName", "%" + escapedName + "%");
        }
        if (departmentId != null) {
            query.setParameter("departmentId", departmentId);
        }
    }
}
