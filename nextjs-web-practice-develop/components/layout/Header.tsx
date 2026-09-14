import Link from 'next/link';
import Image from 'next/image';
import { ROUTES, LABELS } from '@/constants';

/**
 * Component hiển thị thanh điều hướng đầu trang (Header / Navigation Bar).
 * Bao gồm Logo công ty, tên thương hiệu, liên kết về trang danh sách (Top) và đăng xuất.
 *
 * @returns JSX.Element Header giao diện chung
 */
const Header = () => {
  return (
    <nav className="nav-bar">
      <div className="content-main">
        <div className="d-flex">
          <a className="navbar-brand">
            <Image src="/assets/images/Logo-Luvina.svg" title="Logo" alt="logo" width={100} height={50} />
          </a>
          <h5 className="title-brand mr-auto">Luvina Software</h5>
          <ul className="navbar-nav flex-row d-flex">
            <li className="nav-item">
              <Link href={ROUTES.LOGOUT}>{LABELS.BUTTONS.LOGOUT}</Link>
            </li>
            <li className="nav-item">
              <Link href={ROUTES.EMPLOYEES.LIST}>{LABELS.BUTTONS.TOP}</Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Header;
