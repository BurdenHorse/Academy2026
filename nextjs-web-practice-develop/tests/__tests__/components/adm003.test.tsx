import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import ADM003 from '@/components/employees/adm003';
import { useADM003 } from '@/hooks/useADM003';
import { LABELS } from '@/constants';

jest.mock('@/hooks/useADM003');

const mockedUseADM003 = useADM003 as jest.Mock;

describe('ADM003 Component', () => {
  const mockHandleEdit = jest.fn();
  const mockHandleDelete = jest.fn();
  const mockHandleBack = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('hiển thị trạng thái đang tải (Loading)', () => {
    mockedUseADM003.mockReturnValue({
      employee: null,
      isLoading: true,
      isDeleting: false,
      deleteError: null,
      handleEdit: mockHandleEdit,
      handleDelete: mockHandleDelete,
      handleBack: mockHandleBack,
    });

    render(<ADM003 />);
    expect(screen.getByText('読み込み中...')).toBeInTheDocument();
  });

  it('hiển thị đầy đủ thông tin nhân viên và chứng chỉ tiếng Nhật', () => {
    mockedUseADM003.mockReturnValue({
      employee: {
        code: '200',
        employeeId: 1,
        employeeName: 'Nguyễn Văn A',
        employeeBirthDate: '1985/05/15',
        departmentId: 1,
        departmentName: 'Phòng Phát Triển',
        employeeEmail: 'vana@luvina.net',
        employeeTelephone: '0987654321',
        employeeNameKana: 'グエン ヴァン エー',
        employeeLoginId: 'vana',
        certifications: [
          {
            certificationId: 1,
            certificationName: 'N1',
            startDate: '2020/01/01',
            endDate: '2022/01/01',
            score: 165,
          },
        ],
      },
      isLoading: false,
      isDeleting: false,
      deleteError: null,
      handleEdit: mockHandleEdit,
      handleDelete: mockHandleDelete,
      handleBack: mockHandleBack,
    });

    render(<ADM003 />);

    expect(screen.getByText('vana')).toBeInTheDocument();
    expect(screen.getByText('Phòng Phát Triển')).toBeInTheDocument();
    expect(screen.getByText('Nguyễn Văn A')).toBeInTheDocument();
    expect(screen.getByText('グエン ヴァン エー')).toBeInTheDocument();
    expect(screen.getByText('1985/05/15')).toBeInTheDocument();
    expect(screen.getByText('vana@luvina.net')).toBeInTheDocument();
    expect(screen.getByText('0987654321')).toBeInTheDocument();

    // Chứng chỉ tiếng Nhật
    expect(screen.getByText('N1')).toBeInTheDocument();
    expect(screen.getByText('2020/01/01')).toBeInTheDocument();
    expect(screen.getByText('2022/01/01')).toBeInTheDocument();
    expect(screen.getByText('165')).toBeInTheDocument();
  });

  it('hiển thị thông báo lỗi khi xóa thất bại', () => {
    mockedUseADM003.mockReturnValue({
      employee: {
        code: '200',
        employeeId: 1,
        employeeName: 'Nguyễn Văn A',
        employeeBirthDate: '1985/05/15',
        departmentId: 1,
        departmentName: 'Phòng Phát Triển',
        employeeEmail: 'vana@luvina.net',
        employeeTelephone: '0987654321',
        employeeNameKana: 'グエン ヴァン エー',
        employeeLoginId: 'vana',
        certifications: [],
      },
      isLoading: false,
      isDeleting: false,
      deleteError: '該当するユーザは存在していません。',
      handleEdit: mockHandleEdit,
      handleDelete: mockHandleDelete,
      handleBack: mockHandleBack,
    });

    render(<ADM003 />);
    expect(screen.getByText('該当するユーザは存在していません。')).toBeInTheDocument();
  });

  it('kích hoạt các sự kiện nút bấm: Edit, Delete, Back', () => {
    mockedUseADM003.mockReturnValue({
      employee: {
        code: '200',
        employeeId: 1,
        employeeName: 'Nguyễn Văn A',
        employeeBirthDate: '1985/05/15',
        departmentId: 1,
        departmentName: 'Phòng Phát Triển',
        employeeEmail: 'vana@luvina.net',
        employeeTelephone: '0987654321',
        employeeNameKana: 'グエン ヴァン エー',
        employeeLoginId: 'vana',
        certifications: [],
      },
      isLoading: false,
      isDeleting: false,
      deleteError: null,
      handleEdit: mockHandleEdit,
      handleDelete: mockHandleDelete,
      handleBack: mockHandleBack,
    });

    render(<ADM003 />);

    fireEvent.click(screen.getByRole('button', { name: LABELS.BUTTONS.EDIT }));
    expect(mockHandleEdit).toHaveBeenCalledTimes(1);

    fireEvent.click(screen.getByRole('button', { name: LABELS.BUTTONS.DELETE }));
    expect(mockHandleDelete).toHaveBeenCalledTimes(1);

    fireEvent.click(screen.getByRole('button', { name: LABELS.BUTTONS.BACK }));
    expect(mockHandleBack).toHaveBeenCalledTimes(1);
  });
});
