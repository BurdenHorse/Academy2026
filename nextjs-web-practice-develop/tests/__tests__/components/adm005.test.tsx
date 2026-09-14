import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import ADM005 from '@/components/employees/adm005';
import { useADM005 } from '@/hooks/useADM005';
import { LABELS } from '@/constants';

jest.mock('@/hooks/useADM005');

const mockedUseADM005 = useADM005 as jest.Mock;

describe('ADM005 Component', () => {
  const mockHandleSave = jest.fn();
  const mockHandleBack = jest.fn();

  const sampleFormData = {
    employeeLoginId: 'vana',
    departmentId: '1',
    employeeName: 'Nguyễn Văn A',
    employeeNameKana: 'グエン ヴァン エー',
    employeeBirthDate: '1985/05/15',
    employeeEmail: 'vana@luvina.net',
    employeeTelephone: '0987654321',
    employeeLoginPassword: '',
    employeeLoginPasswordConfirm: '',
    certificationId: '1',
    certificationStartDate: '2020/01/01',
    certificationEndDate: '2022/01/01',
    employeeCertificationScore: '165',
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('không render gì (null) khi formData là null', () => {
    mockedUseADM005.mockReturnValue({
      formData: null,
      departmentName: '',
      certificationName: '',
      isSubmitting: false,
      submitError: null,
      handleSave: mockHandleSave,
      handleBack: mockHandleBack,
    });

    const { container } = render(<ADM005 />);
    expect(container.firstChild).toBeNull();
  });

  it('hiển thị đầy đủ thông tin nhân viên và chứng chỉ tiếng Nhật', () => {
    mockedUseADM005.mockReturnValue({
      formData: sampleFormData,
      departmentName: 'Phòng Phát Triển',
      certificationName: 'N1',
      isSubmitting: false,
      submitError: null,
      handleSave: mockHandleSave,
      handleBack: mockHandleBack,
    });

    render(<ADM005 />);

    expect(screen.getByText('vana')).toBeInTheDocument();
    expect(screen.getByText('Phòng Phát Triển')).toBeInTheDocument();
    expect(screen.getByText('Nguyễn Văn A')).toBeInTheDocument();
    expect(screen.getByText('グエン ヴァン エー')).toBeInTheDocument();
    expect(screen.getByText('1985/05/15')).toBeInTheDocument();
    expect(screen.getByText('vana@luvina.net')).toBeInTheDocument();
    expect(screen.getByText('0987654321')).toBeInTheDocument();
    expect(screen.getByText('N1')).toBeInTheDocument();
    expect(screen.getByText('2020/01/01')).toBeInTheDocument();
    expect(screen.getByText('2022/01/01')).toBeInTheDocument();
    expect(screen.getByText('165')).toBeInTheDocument();
  });

  it('kích hoạt handleSave khi bấm nút OK', () => {
    mockedUseADM005.mockReturnValue({
      formData: sampleFormData,
      departmentName: 'Phòng Phát Triển',
      certificationName: 'N1',
      isSubmitting: false,
      submitError: null,
      handleSave: mockHandleSave,
      handleBack: mockHandleBack,
    });

    render(<ADM005 />);

    fireEvent.click(screen.getByRole('button', { name: LABELS.BUTTONS.OK }));
    expect(mockHandleSave).toHaveBeenCalledTimes(1);
  });

  it('kích hoạt handleBack khi bấm nút Back', () => {
    mockedUseADM005.mockReturnValue({
      formData: sampleFormData,
      departmentName: 'Phòng Phát Triển',
      certificationName: 'N1',
      isSubmitting: false,
      submitError: null,
      handleSave: mockHandleSave,
      handleBack: mockHandleBack,
    });

    render(<ADM005 />);

    fireEvent.click(screen.getByRole('button', { name: LABELS.BUTTONS.BACK }));
    expect(mockHandleBack).toHaveBeenCalledTimes(1);
  });

  it('hiển thị submitError khi có lỗi hệ thống', () => {
    mockedUseADM005.mockReturnValue({
      formData: sampleFormData,
      departmentName: 'Phòng Phát Triển',
      certificationName: 'N1',
      isSubmitting: false,
      submitError: 'Có lỗi xảy ra',
      handleSave: mockHandleSave,
      handleBack: mockHandleBack,
    });

    render(<ADM005 />);
    expect(screen.getByText('Có lỗi xảy ra')).toBeInTheDocument();
  });

  it('vô hiệu hóa các nút bấm khi isSubmitting là true', () => {
    mockedUseADM005.mockReturnValue({
      formData: sampleFormData,
      departmentName: 'Phòng Phát Triển',
      certificationName: 'N1',
      isSubmitting: true,
      submitError: null,
      handleSave: mockHandleSave,
      handleBack: mockHandleBack,
    });

    render(<ADM005 />);

    expect(screen.getByRole('button', { name: LABELS.BUTTONS.SUBMITTING })).toBeDisabled();
    expect(screen.getByRole('button', { name: LABELS.BUTTONS.BACK })).toBeDisabled();
  });
});

