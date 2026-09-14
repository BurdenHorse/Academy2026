'use client';

import React from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useADM002 } from '@/hooks/useADM002';
import { ROUTES, MESSAGES, LABELS, STORAGE_KEYS } from '@/constants';

/**
 * Component hiển thị toàn bộ giao diện màn hình Danh sách nhân viên (ADM002).
 * Sử dụng custom hook useADM002.
 */
export default function ADM002() {
  const router = useRouter();

  const {
    departments,
    employees,
    isLoading,
    error,
    searchInput,
    setSearchInput,
    handleSearch,
    handlePageChange,
    handleSort,
    getSortIcon,
    totalPages,
    currentPage,
    truncateText,
  } = useADM002();

  /**
   * Render giao diện các nút điều hướng phân trang.
   */
  const renderPagination = () => {
    if (totalPages <= 1) return null;

    return (
      <div className="pagin mt-3 d-flex justify-content-end">
        {/* Nút lùi: disabled khi ở trang đầu */}
        <button
          className={`btn btn-sm btn-pre btn-falcon-default ${currentPage === 1 ? 'btn-disabled' : ''}`}
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
        >
          &lt;
        </button>

        {/* Trang đầu (1) */}
        <button
          onClick={() => handlePageChange(1)}
          className={`btn btn-sm btn-falcon-default ${currentPage === 1 ? 'text-primary fw-bold text-decoration-underline' : ''}`}
          style={currentPage === 1 ? { textDecoration: 'underline' } : {}}
        >
          1
        </button>

        {/* Dấu ... trước trang trước (khi currentPage > 3) */}
        {currentPage > 3 && <span className="btn btn-sm btn-falcon-default">...</span>}

        {/* Trang trước (nếu có và khác trang 1) */}
        {currentPage - 1 > 1 && (
          <button
            onClick={() => handlePageChange(currentPage - 1)}
            className="btn btn-sm btn-falcon-default"
          >
            {currentPage - 1}
          </button>
        )}

        {/* Trang hiện tại (khi không phải trang đầu và không phải trang cuối) */}
        {currentPage > 1 && currentPage < totalPages && (
          <button
            onClick={() => handlePageChange(currentPage)}
            className="btn btn-sm btn-falcon-default text-primary fw-bold text-decoration-underline"
            style={{ textDecoration: 'underline' }}
          >
            {currentPage}
          </button>
        )}

        {/* Trang sau (nếu có và khác trang cuối) */}
        {currentPage + 1 < totalPages && (
          <button
            onClick={() => handlePageChange(currentPage + 1)}
            className="btn btn-sm btn-falcon-default"
          >
            {currentPage + 1}
          </button>
        )}

        {/* Dấu ... sau trang sau (khi currentPage < totalPages - 2) */}
        {currentPage < totalPages - 2 && <span className="btn btn-sm btn-falcon-default">...</span>}

        {/* Trang cuối (totalPages) */}
        {totalPages > 1 && (
          <button
            onClick={() => handlePageChange(totalPages)}
            className={`btn btn-sm btn-falcon-default ${currentPage === totalPages ? 'text-primary fw-bold text-decoration-underline' : ''}`}
            style={currentPage === totalPages ? { textDecoration: 'underline' } : {}}
          >
            {totalPages}
          </button>
        )}

        {/* Nút tiến: disabled khi ở trang cuối */}
        <button
          className={`btn btn-sm btn-next btn-falcon-default ${currentPage === totalPages ? 'btn-disabled' : ''}`}
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
        >
          &gt;
        </button>
      </div>
    );
  };

  return (
    <>
      {/* ===== Phần tìm kiếm (Search Form) ===== */}
      <div className="search-memb">
        <h1 className="title">{LABELS.TITLES.ADM002_SEARCH_GUIDE}</h1>
        <form
          className="c-form"
          onSubmit={(e) => {
            e.preventDefault();
            handleSearch();
          }}
        >
          <ul className="d-flex align-items-center">
            <li className="form-group row mb-0 mr-3">
              <label className="col-form-label">{LABELS.FIELDS.FULL_NAME}:</label>
              <div className="col-sm">
                <input
                  type="text"
                  maxLength={125}
                  value={searchInput.employee_name}
                  onChange={(e) =>
                    setSearchInput({ ...searchInput, employee_name: e.target.value })
                  }
                  className="form-control"
                />
              </div>
            </li>
            <li className="form-group row mb-0 mr-3">
              <label className="col-form-label">{LABELS.FIELDS.GROUP}:</label>
              <div className="col-sm">
                <select
                  value={searchInput.department_id}
                  onChange={(e) =>
                    setSearchInput({ ...searchInput, department_id: e.target.value })
                  }
                  className="form-control"
                >
                  <option value="">{LABELS.OPTIONS.ALL}</option>
                  {departments.map((dept) => (
                    <option key={dept.departmentId} value={dept.departmentId}>
                      {dept.departmentName}
                    </option>
                  ))}
                </select>
              </div>
            </li>
            <li className="form-group row mb-0">
              <div className="btn-group">
                <button type="submit" className="btn btn-primary btn-sm mr-2">
                  {LABELS.BUTTONS.SEARCH}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    if (typeof window !== 'undefined') {
                      sessionStorage.removeItem(STORAGE_KEYS.FORM_DATA);
                    }
                    router.push(ROUTES.EMPLOYEES.ADD_EDIT);
                  }}
                  className="btn btn-secondary btn-sm"
                >
                  {LABELS.BUTTONS.ADD_NEW}
                </button>
              </div>
            </li>
          </ul>
        </form>
      </div>

      {/* ===== Phần hiển thị lỗi (Error Alert) ===== */}
      {error && <div className="alert alert-danger mt-3">{error}</div>}

      {/* ===== Phần bảng danh sách nhân viên (Employee Table) ===== */}

      <div className="row row-table mt-4">
        {isLoading ? null : employees.length === 0 ? (
          <div className="text-center w-100 py-5">{MESSAGES.MSG.MSG005}</div>
        ) : (
          <div className="css-grid-table box-shadow w-100">
            <div className="css-grid-table-header">
              <div>{LABELS.FIELDS.EMPLOYEE_ID}</div>
              <div style={{ cursor: 'pointer' }} onClick={() => handleSort('ord_employee_name')}>
                {LABELS.FIELDS.FULL_NAME} {getSortIcon('ord_employee_name')}
              </div>
              <div>{LABELS.FIELDS.BIRTH_DATE}</div>
              <div>{LABELS.FIELDS.GROUP}</div>
              <div>{LABELS.FIELDS.EMAIL}</div>
              <div>{LABELS.FIELDS.TELEPHONE}</div>
              <div
                style={{ cursor: 'pointer' }}
                onClick={() => handleSort('ord_certification_name')}
              >
                {LABELS.TITLES.JAPANESE_SKILL} {getSortIcon('ord_certification_name')}
              </div>
              <div style={{ cursor: 'pointer' }} onClick={() => handleSort('ord_end_date')}>
                {LABELS.FIELDS.EXPIRATION_DATE} {getSortIcon('ord_end_date')}
              </div>
              <div>{LABELS.FIELDS.SCORE}</div>
            </div>

            <div className="css-grid-table-body">
              {employees.map((emp) => (
                <React.Fragment key={`${emp.employeeId}-${emp.certificationName || 'none'}`}>
                  <div className="bor-l-none text-center">
                    <Link href={`${ROUTES.EMPLOYEES.DETAIL}?id=${emp.employeeId}`}>
                      {emp.employeeId}
                    </Link>
                  </div>
                  <div title={emp.employeeName}>{truncateText(emp.employeeName, 22)}</div>
                  <div>{emp.employeeBirthDate || ''}</div>
                  <div title={emp.departmentName}>{truncateText(emp.departmentName, 22)}</div>
                  <div title={emp.employeeEmail}>{truncateText(emp.employeeEmail, 22)}</div>
                  <div>{truncateText(emp.employeeTelephone, 22)}</div>
                  <div title={emp.certificationName || ''}>
                    {truncateText(emp.certificationName, 22)}
                  </div>
                  <div>{emp.endDate || ''}</div>
                  <div>{emp.score || ''}</div>
                </React.Fragment>
              ))}
            </div>
          </div>
        )}

        {/* ===== Phần phân trang (Pagination) ===== */}
        {!isLoading && employees.length > 0 && renderPagination()}
      </div>
    </>
  );
}

export { ADM002 };
