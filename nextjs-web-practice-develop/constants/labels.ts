/**
 * Định nghĩa tập trung tất cả các tiêu đề, nhãn (labels), placeholder và tên nút bấm của giao diện.
 */
export const LABELS = {
  // === Tiêu đề màn hình ===
  TITLES: {
    /** Tiêu đề hướng dẫn tìm kiếm trên ADM002 */
    ADM002_SEARCH_GUIDE: '会員名称で会員を検索します。検索条件無しの場合は全て表示されます。',
    /** Tiêu đề xem chi tiết / xác nhận thông tin */
    CONFIRM_INFO: '情報確認',
    /** Hướng dẫn lưu ở màn hình confirm */
    CONFIRM_SUBTITLE: '入力された情報をＯＫボタンクリックでＤＢへ保存してください',
    /** Tiêu đề màn hình thêm mới / chỉnh sửa */
    EMPLOYEE_EDIT: '会員情報編集',
    /** Tiêu đề nhóm chứng chỉ tiếng Nhật */
    JAPANESE_SKILL: '日本語能力',
  },

  // === Nhãn các trường form ===
  FIELDS: {
    EMPLOYEE_ID: 'ID',
    ACCOUNT_NAME: 'アカウント名',
    GROUP: 'グループ',
    FULL_NAME: '氏名',
    FULL_NAME_KANA: 'カタカナ氏名',
    BIRTH_DATE: '生年月日',
    EMAIL: 'メールアドレス',
    TELEPHONE: '電話番号',
    PASSWORD: 'パスワード',
    PASSWORD_CONFIRM: 'パスワード（確認）',
    CERTIFICATION: '資格',
    CERTIFICATION_START_DATE: '資格交付日',
    EXPIRATION_DATE: '失効日',
    SCORE: '点数',
  },

  // === Lựa chọn dropdown ===
  OPTIONS: {
    ALL: '全て',
    PLEASE_SELECT: '選択してください',
  },

  // === Tên nút bấm ===
  BUTTONS: {
    SEARCH: '検索',
    ADD_NEW: '新規追加',
    EDIT: '編集',
    DELETE: '削除',
    CONFIRM: '確認',
    BACK: '戻る',
    CANCEL: 'キャンセル',
    OK: 'OK',
    LOGIN: 'ログイン',
    LOGOUT: 'ログアウト',
    TOP: 'トップ',
    SUBMITTING: '処理中...',
  },

  // === Placeholders ===
  PLACEHOLDERS: {
    ACCOUNT_NAME: 'アカウント名:',
    PASSWORD: 'パスワード:',
    DATE_FORMAT: 'yyyy/MM/dd',
  },
} as const;
