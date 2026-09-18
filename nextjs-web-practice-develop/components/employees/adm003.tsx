'use client';

import React from 'react';
import { useADM003 } from '@/hooks/useADM003';
import { LABELS } from '@/constants';

/**
 * Component hiển thị toàn bộ giao diện màn hình Chi tiết nhân viên (ADM003).
 */
export default function ADM003() {
  const {
    employee,
    isLoading,
    isDeleting,
    deleteError,
    handleEdit,
    handleDelete,
    handleBack,
  } = useADM003();

  if (isLoading) {
    return (
      <div className="row">
        <div className="c-form box-shadow text-center p-4">
          <div>{LABELS.STATUS.LOADING}</div>
        </div>
      </div>
    );
  }

  if (!employee) {
    return null;
  }

  // Lấy chứng chỉ đầu tiên (hoặc cao nhất theo sắp xếp backend) nếu có
  const primaryCert =
    employee.certifications && employee.certifications.length > 0
      ? employee.certifications[0]
      : null;

  return (
    <div className="row">
      <form className="c-form box-shadow">
        <ul className="show-data">
          {/* ===== Phần tiêu đề (Title) ===== */}
          <li className="title">{LABELS.TITLES.CONFIRM_INFO}</li>

          {/* Vùng hiển thị lỗi khi xóa thất bại */}
          {deleteError && (
            <li className="form-group row d-flex">
              <div
                className="col-sm-12 text-danger"
                style={{ color: '#d9534f', fontWeight: 'bold', padding: '8px 15px' }}
              >
                {deleteError}
              </div>
            </li>
          )}

          {/* ===== Phần thông tin cơ bản nhân viên ===== */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.ACCOUNT_NAME}</label>
            <div className="col-sm col-sm-10">{employee.employeeLoginId}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.GROUP}</label>
            <div className="col-sm col-sm-10">{employee.departmentName}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.FULL_NAME}</label>
            <div className="col-sm col-sm-10">{employee.employeeName}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.FULL_NAME_KANA}</label>
            <div className="col-sm col-sm-10">{employee.employeeNameKana}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.BIRTH_DATE}</label>
            <div className="col-sm col-sm-10">{employee.employeeBirthDate}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.EMAIL}</label>
            <div className="col-sm col-sm-10">{employee.employeeEmail}</div>
          </li>
          <li className="form-group row d-flex bor-none">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.TELEPHONE}</label>
            <div className="col-sm col-sm-10">{employee.employeeTelephone}</div>
          </li>

          {/* Cụm thông tin Trình độ tiếng Nhật (luôn hiển thị label, để trống nếu không có chứng chỉ) */}
          <li className="title mt-12">
            <a href="#!">{LABELS.TITLES.JAPANESE_SKILL}</a>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.CERTIFICATION}</label>
            <div className="col-sm col-sm-10">{primaryCert?.certificationName || ''}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.CERTIFICATION_START_DATE}</label>
            <div className="col-sm col-sm-10">{primaryCert?.startDate || ''}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.EXPIRATION_DATE}</label>
            <div className="col-sm col-sm-10">{primaryCert?.endDate || ''}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.SCORE}</label>
            <div className="col-sm col-sm-10">
              {primaryCert?.score !== undefined && primaryCert?.score !== null
                ? String(primaryCert.score)
                : ''}
            </div>
          </li>

          {/* Cụm nút bấm: 編集, 削除, 戻る/キャンセル */}
          <li className="form-group row d-flex">
            <div className="btn-group col-sm col-sm-10 ml">
              <button
                type="button"
                onClick={handleEdit}
                disabled={isDeleting}
                className="btn btn-primary btn-sm"
              >
                {LABELS.BUTTONS.EDIT}
              </button>
              <button
                type="button"
                onClick={handleDelete}
                disabled={isDeleting}
                className="btn btn-secondary btn-sm"
              >
                {isDeleting ? LABELS.BUTTONS.SUBMITTING : LABELS.BUTTONS.DELETE}
              </button>
              <button
                type="button"
                onClick={handleBack}
                disabled={isDeleting}
                className="btn btn-secondary btn-sm"
              >
                {LABELS.BUTTONS.BACK}
              </button>
            </div>
          </li>
        </ul>
      </form>
    </div>
  );
}

export { ADM003 };
