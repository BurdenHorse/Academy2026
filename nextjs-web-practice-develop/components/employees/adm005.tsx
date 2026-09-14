'use client';

import React from 'react';
import { useADM005 } from '@/hooks/useADM005';
import { LABELS } from '@/constants';

/**
 * Component hiển thị toàn bộ giao diện màn hình Xác nhận thông tin nhân viên (ADM005).
 * Sử dụng custom hook useADM005.
 */
export default function ADM005() {
  const {
    formData,
    departmentName,
    certificationName,
    isSubmitting,
    submitError,
    handleSave,
    handleBack,
  } = useADM005();

  if (!formData) {
    return null;
  }

  return (
    <div className="row">
      <form className="c-form box-shadow" onSubmit={(e) => e.preventDefault()}>
        <ul className="show-data">
          {/* ===== Phần tiêu đề và subtitle ===== */}
          <li className="title">
            <p>{LABELS.TITLES.CONFIRM_INFO}</p>
            <p>{LABELS.TITLES.CONFIRM_SUBTITLE}</p>
          </li>

          {/* Hiển thị lỗi hệ thống nếu submit thất bại */}
          {/* ===== Phần hiển thị lỗi submit ===== */}
          {submitError && (
            <li className="form-group row d-flex">
              <div className="col-sm col-sm-12 text-danger font-weight-bold">
                {submitError}
              </div>
            </li>
          )}

          {/* ===== Phần thông tin cơ bản nhân viên ===== */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.ACCOUNT_NAME}</label>
            <div className="col-sm col-sm-10">{formData.employeeLoginId}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.GROUP}</label>
            <div className="col-sm col-sm-10">{departmentName}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.FULL_NAME}</label>
            <div className="col-sm col-sm-10">{formData.employeeName}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.FULL_NAME_KANA}</label>
            <div className="col-sm col-sm-10">{formData.employeeNameKana}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.BIRTH_DATE}</label>
            <div className="col-sm col-sm-10">{formData.employeeBirthDate}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.EMAIL}</label>
            <div className="col-sm col-sm-10">{formData.employeeEmail}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.TELEPHONE}</label>
            <div className="col-sm col-sm-10">{formData.employeeTelephone}</div>
          </li>

          {/* Cụm thông tin Trình độ tiếng Nhật */}
          <li className="title mt-12">
            <a href="#!">{LABELS.TITLES.JAPANESE_SKILL}</a>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.CERTIFICATION}</label>
            <div className="col-sm col-sm-10">{certificationName}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.CERTIFICATION_START_DATE}</label>
            <div className="col-sm col-sm-10">{formData.certificationStartDate}</div>
          </li>
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.EXPIRATION_DATE}</label>
            <div className="col-sm col-sm-10">{formData.certificationEndDate}</div>
          </li>
          <li className="form-group row d-flex bor-none">
            <label className="col-form-label col-sm-2">{LABELS.FIELDS.SCORE}</label>
            <div className="col-sm col-sm-10">{formData.employeeCertificationScore}</div>
          </li>

          {/* ===== Phần nút bấm (OK / Cancel) ===== */}
          <li className="form-group row d-flex">
            <div className="btn-group col-sm col-sm-10 ml">
              <button
                type="button"
                onClick={handleSave}
                disabled={isSubmitting}
                className="btn btn-primary btn-sm"
              >
                {isSubmitting ? LABELS.BUTTONS.SUBMITTING : LABELS.BUTTONS.OK}
              </button>
              <button
                type="button"
                onClick={handleBack}
                disabled={isSubmitting}
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

export { ADM005 };
