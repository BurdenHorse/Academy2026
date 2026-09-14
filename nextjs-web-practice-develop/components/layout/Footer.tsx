/**
 * Component hiển thị thông tin chân trang (Footer).
 * Chứa bản quyền Copyright cố định của công ty.
 *
 * @returns JSX.Element Footer giao diện chung
 */
const Footer = () => {
  return (
    <footer className="footer">
      <div className="content-main">
        <p>Copyright © 2010 ルビナソフトウエア株式会社. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;
