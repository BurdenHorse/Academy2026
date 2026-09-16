'use client';

import React from 'react';
import DatePicker from 'react-datepicker';
import { useADM004 } from '@/hooks/useADM004';
import { parseStringToDate } from '@/utils/date';
import { LABELS, MESSAGES } from '@/constants';

/**
 * Component hiển thị toàn bộ giao diện màn hình Thêm mới / Chỉnh sửa nhân viên (ADM004).
 * Sử dụng custom hook useADM004.
 */
export default function ADM004() {
  const {
    departments,
    departmentError,
    certifications,
    certificationError,
    formData,
    errors,
    serverError,
    isLoading,
    isSubmitting,
    isEditMode,
    isCertDisabled,
    accountNameRef,
    birthDateRef,
    certificationStartDateRef,
    certificationEndDateRef,
    handleInputChange,
    handleInputBlur,
    handleCertificationChange,
    handleDateChange,
    handleConfirm,
    handleBack,
  } = useADM004();

  if (isLoading) {
    return (
      <div className="row">
        <div className="c-form box-shadow text-center p-4">
          <div>読み込み中...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="row">
      <form
        className="c-form box-shadow"
        onSubmit={(e) => {
          e.preventDefault();
          handleConfirm();
        }}
      >
        <ul>
          <li className="title">{LABELS.TITLES.EMPLOYEE_EDIT}</li>
          {serverError &&
            serverError !== MESSAGES.ERRORS.SYSTEM_ERROR &&
            serverError !== MESSAGES.ERRORS.ER015() &&
            serverError !== MESSAGES.ERRORS.ER023() && (
              <li className="box-err">
                <div className="alert alert-danger mb-3">{serverError}</div>
              </li>
            )}

          {/* 1. Account Name */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.ACCOUNT_NAME}:<span className="note-red">*</span>
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <input
                ref={accountNameRef}
                type="text"
                readOnly={isEditMode}
                tabIndex={isEditMode ? -1 : 0}
                value={formData.employeeLoginId}
                onChange={(e) => !isEditMode && handleInputChange('employeeLoginId', e.target.value)}
                onBlur={(e) => !isEditMode && handleInputBlur('employeeLoginId', e.target.value)}
                className={`form-control ${isEditMode ? 'input-readonly' : ''} ${errors.employeeLoginId ? 'is-invalid border-danger' : ''}`}
              />
              {errors.employeeLoginId && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.employeeLoginId}
                </span>
              )}
            </div>
          </li>

          {/* 2. Group / Department */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.GROUP}:<span className="note-red">*</span>
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <select
                value={formData.departmentId}
                onChange={(e) => handleInputChange('departmentId', e.target.value)}
                onBlur={(e) => handleInputBlur('departmentId', e.target.value)}
                className={`form-control ${errors.departmentId ? 'is-invalid border-danger' : ''}`}
              >
                <option value="">{LABELS.OPTIONS.PLEASE_SELECT}</option>
                {departments.map((dept) => (
                  <option key={dept.departmentId} value={dept.departmentId}>
                    {dept.departmentName}
                  </option>
                ))}
              </select>
              {errors.departmentId && (
                <span className="text-danger font-sm d-block mt-1">{errors.departmentId}</span>
              )}
            </div>
          </li>

          {/* 3. Full Name */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.FULL_NAME}:<span className="note-red">*</span>
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <input
                type="text"
                value={formData.employeeName}
                onChange={(e) => handleInputChange('employeeName', e.target.value)}
                onBlur={(e) => handleInputBlur('employeeName', e.target.value)}
                className={`form-control ${errors.employeeName ? 'is-invalid border-danger' : ''}`}
              />
              {errors.employeeName && (
                <span className="text-danger font-sm d-block mt-1">{errors.employeeName}</span>
              )}
            </div>
          </li>

          {/* 4. Full Name Kana */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.FULL_NAME_KANA}:<span className="note-red">*</span>
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <input
                type="text"
                value={formData.employeeNameKana}
                onChange={(e) => handleInputChange('employeeNameKana', e.target.value)}
                onBlur={(e) => handleInputBlur('employeeNameKana', e.target.value)}
                className={`form-control ${errors.employeeNameKana ? 'is-invalid border-danger' : ''}`}
              />
              {errors.employeeNameKana && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.employeeNameKana}
                </span>
              )}
            </div>
          </li>

          {/* 5. Birthday */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.BIRTH_DATE}:<span className="note-red">*</span>
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <div className="datepicker-wrapper">
                <DatePicker
                  ref={birthDateRef}
                  placeholderText={LABELS.PLACEHOLDERS.DATE_FORMAT}
                  selected={parseStringToDate(formData.employeeBirthDate)}
                  onChange={(date: Date | null) => handleDateChange('employeeBirthDate', date)}
                  onBlur={() => handleInputBlur('employeeBirthDate')}
                  dateFormat={LABELS.PLACEHOLDERS.DATE_FORMAT}
                  onKeyDown={(e) => e.preventDefault()}
                  showMonthDropdown
                  showYearDropdown
                  dropdownMode="select"
                  autoComplete="off"
                  popperPlacement="bottom-end"
                  className={`form-control ${errors.employeeBirthDate ? 'is-invalid border-danger' : ''}`}
                />
                <span
                  className="glyphicon glyphicon-calendar"
                  onClick={() => {
                    birthDateRef.current?.setFocus();
                    birthDateRef.current?.setOpen(true);
                  }}
                ></span>
              </div>
              {errors.employeeBirthDate && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.employeeBirthDate}
                </span>
              )}
            </div>
          </li>

          {/* 6. Email */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.EMAIL}:<span className="note-red">*</span>
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <input
                type="email"
                value={formData.employeeEmail}
                onChange={(e) => handleInputChange('employeeEmail', e.target.value)}
                onBlur={(e) => handleInputBlur('employeeEmail', e.target.value)}
                className={`form-control ${errors.employeeEmail ? 'is-invalid border-danger' : ''}`}
              />
              {errors.employeeEmail && (
                <span className="text-danger font-sm d-block mt-1">{errors.employeeEmail}</span>
              )}
            </div>
          </li>

          {/* 7. Telephone */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.TELEPHONE}:<span className="note-red">*</span>
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <input
                type="text"
                value={formData.employeeTelephone}
                onChange={(e) => handleInputChange('employeeTelephone', e.target.value)}
                onBlur={(e) => handleInputBlur('employeeTelephone', e.target.value)}
                className={`form-control ${errors.employeeTelephone ? 'is-invalid border-danger' : ''}`}
              />
              {errors.employeeTelephone && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.employeeTelephone}
                </span>
              )}
            </div>
          </li>

          {/* 8. Password */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.PASSWORD}:
                {!isEditMode && <span className="note-red">*</span>}
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <input
                type="password"
                value={formData.employeeLoginPassword}
                onChange={(e) => handleInputChange('employeeLoginPassword', e.target.value)}
                onBlur={(e) => handleInputBlur('employeeLoginPassword', e.target.value)}
                className={`form-control ${errors.employeeLoginPassword ? 'is-invalid border-danger' : ''}`}
              />
              {errors.employeeLoginPassword && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.employeeLoginPassword}
                </span>
              )}
            </div>
          </li>

          {/* 9. Password Confirm */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.PASSWORD_CONFIRM}:
                {!isEditMode && <span className="note-red">*</span>}
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <input
                type="password"
                value={formData.employeeLoginPasswordConfirm}
                onChange={(e) =>
                  handleInputChange('employeeLoginPasswordConfirm', e.target.value)
                }
                onBlur={(e) =>
                  handleInputBlur('employeeLoginPasswordConfirm', e.target.value)
                }
                className={`form-control ${errors.employeeLoginPasswordConfirm ? 'is-invalid border-danger' : ''}`}
              />
              {errors.employeeLoginPasswordConfirm && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.employeeLoginPasswordConfirm}
                </span>
              )}
            </div>
          </li>

          {/* ================= Japanese Skill Section ================= */}
          <li className="title mt-12">
            <a href="#!">{LABELS.TITLES.JAPANESE_SKILL}</a>
          </li>

          {/* 10. Certification Dropdown */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">{LABELS.FIELDS.CERTIFICATION}:</i>
            </label>
            <div className="col-sm col-sm-10">
              <select
                value={formData.certificationId}
                onChange={(e) => handleCertificationChange(e.target.value)}
                onBlur={(e) => handleInputBlur('certificationId', e.target.value)}
                className="form-control"
              >
                <option value="">{LABELS.OPTIONS.PLEASE_SELECT}</option>
                {certifications.map((cert) => (
                  <option key={cert.certificationId} value={cert.certificationId}>
                    {cert.certificationName}
                  </option>
                ))}
              </select>
            </div>
          </li>

          {/* 11. Certification Start Date */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.CERTIFICATION_START_DATE}:
                {!isCertDisabled && <span className="note-red">*</span>}
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <div className="datepicker-wrapper">
                <DatePicker
                  ref={certificationStartDateRef}
                  placeholderText={LABELS.PLACEHOLDERS.DATE_FORMAT}
                  selected={parseStringToDate(formData.certificationStartDate)}
                  onChange={(date: Date | null) =>
                    handleDateChange('certificationStartDate', date)
                  }
                  onBlur={() => handleInputBlur('certificationStartDate')}
                  dateFormat={LABELS.PLACEHOLDERS.DATE_FORMAT}
                  disabled={isCertDisabled}
                  onKeyDown={(e) => e.preventDefault()}
                  showMonthDropdown
                  showYearDropdown
                  dropdownMode="select"
                  autoComplete="off"
                  popperPlacement="bottom-end"
                  className={`form-control ${errors.certificationStartDate ? 'is-invalid border-danger' : ''}`}
                />
                <span
                  className="glyphicon glyphicon-calendar"
                  onClick={() => {
                    if (!isCertDisabled) {
                      certificationStartDateRef.current?.setFocus();
                      certificationStartDateRef.current?.setOpen(true);
                    }
                  }}
                  style={{ cursor: isCertDisabled ? 'not-allowed' : 'pointer' }}
                ></span>
              </div>
              {errors.certificationStartDate && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.certificationStartDate}
                </span>
              )}
            </div>
          </li>

          {/* 12. Certification End Date */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.EXPIRATION_DATE}:
                {!isCertDisabled && <span className="note-red">*</span>}
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <div className="datepicker-wrapper">
                <DatePicker
                  ref={certificationEndDateRef}
                  placeholderText={LABELS.PLACEHOLDERS.DATE_FORMAT}
                  selected={parseStringToDate(formData.certificationEndDate)}
                  onChange={(date: Date | null) => handleDateChange('certificationEndDate', date)}
                  onBlur={() => handleInputBlur('certificationEndDate')}
                  dateFormat={LABELS.PLACEHOLDERS.DATE_FORMAT}
                  disabled={isCertDisabled}
                  onKeyDown={(e) => e.preventDefault()}
                  showMonthDropdown
                  showYearDropdown
                  dropdownMode="select"
                  autoComplete="off"
                  popperPlacement="bottom-end"
                  className={`form-control ${errors.certificationEndDate ? 'is-invalid border-danger' : ''}`}
                />
                <span
                  className="glyphicon glyphicon-calendar"
                  onClick={() => {
                    if (!isCertDisabled) {
                      certificationEndDateRef.current?.setFocus();
                      certificationEndDateRef.current?.setOpen(true);
                    }
                  }}
                  style={{ cursor: isCertDisabled ? 'not-allowed' : 'pointer' }}
                ></span>
              </div>
              {errors.certificationEndDate && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.certificationEndDate}
                </span>
              )}
            </div>
          </li>

          {/* 13. Score */}
          <li className="form-group row d-flex">
            <label className="col-form-label col-sm-2">
              <i className="relative">
                {LABELS.FIELDS.SCORE}:
                {!isCertDisabled && <span className="note-red">*</span>}
              </i>
            </label>
            <div className="col-sm col-sm-10">
              <input
                type="text"
                disabled={isCertDisabled}
                value={formData.employeeCertificationScore}
                onChange={(e) =>
                  handleInputChange('employeeCertificationScore', e.target.value)
                }
                onBlur={(e) =>
                  handleInputBlur('employeeCertificationScore', e.target.value)
                }
                className={`form-control ${errors.employeeCertificationScore ? 'is-invalid border-danger' : ''}`}
              />
              {errors.employeeCertificationScore && (
                <span className="text-danger font-sm d-block mt-1">
                  {errors.employeeCertificationScore}
                </span>
              )}
            </div>
          </li>

          {/* Buttons */}
          <li className="form-group row d-flex">
            <div className="btn-group col-sm col-sm-10 ml">
              <button type="submit" disabled={isSubmitting} className="btn btn-primary btn-sm">
                {isSubmitting ? LABELS.BUTTONS.SUBMITTING : LABELS.BUTTONS.CONFIRM}
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

export { ADM004 };
