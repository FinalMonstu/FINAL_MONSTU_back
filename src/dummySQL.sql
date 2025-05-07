INSERT INTO `exception_log`
(`id`, `message`, `created_at`, `level`, `service`, `class_name`, `method_name`, `line_num`, `exception_type`, `request_url`, `http_status`)
VALUES
    (9998, 'Database connection timeout when fetching translations', '2025-05-06 09:05:12', 'ERROR',
     'TranslationService', 'com.icetea.MonStu.service.TranslationService', 'translateWord', 128,
     'TimeoutException', '/api/translate', '504'),
    (9999, 'Failed to parse JSON request payload', '2025-05-06 09:10:45', 'ERROR',
     'ApiController', 'com.icetea.MonStu.controller.ApiController', 'handleRequest', 56,
     'JsonParseException', '/api/messages', '400');


-- 1) Member 테이블 더미 데이터 (ID 9999부터 시작)
INSERT INTO `member`
(`id`,   `email`,             `password`,    `nick_name`,    `phone_number`,    `created_at`,         `updated_at`,         `status`,   `role`,   `country_code`)
VALUES
    ( 9999, 'alice@example.com',  'pass1234',    'Alice',        '010-1234-0001',   '2025-05-01 10:00:00', '2025-05-01 12:00:00', 'ACTIVE',   'MEMBER',   'KOR'),
    (10000, 'bob@example.com',    'secure!@#$',  'Bob',          '010-1234-0002',   '2025-05-02 11:30:00', '2025-05-02 11:30:00', 'ACTIVE',  'MEMBER',   'USA'),
    (10001, 'admin@monstu.com',   'AdminPass$$', 'Administrator','010-1234-9999',   '2025-05-03 09:15:00', '2025-05-03 17:45:00', 'ACTIVE',   'ADMIN',  'JPN');

-- 2) History 테이블 더미 데이터 (ID 9999부터 시작)
INSERT INTO `history`
(`id`,   `target`,             `genre`,      `language_code`)
VALUES
    ( 9999, 'Hello, world!',      'SENTENCE',   'EN'),
    (10000, '안녕하세요',            'WORD',   'KO'),
    (10001, '今日はいい天気ですね。','SENTENCE',  'JA');

-- 3) Nouncement 테이블 더미 데이터 (ID 9999부터 시작)
INSERT INTO `nouncement`
(`id`,   `title`,            `content`,                                                   `created_at`,         `modified_at`,         `is_public`, `is_important`, `view_count`)
VALUES
    ( 9999, '서비스 점검 안내',    '5월 10일(토) 오전 2시부터 4시까지 서비스 점검이 진행됩니다.', '2025-05-05 14:00:00','2025-05-05 14:00:00', TRUE,        FALSE,          123),
    (10000,'새 기능 추가 안내',    '단어 자동 번역 기능이 새롭게 추가되었습니다!',                '2025-05-04 09:30:00','2025-05-04 09:30:00', TRUE,        TRUE,           456),
    (10001,'이벤트 당첨자 발표',   '5월 월간 퀴즈 이벤트 당첨자를 발표합니다. 축하드립니다!',        '2025-05-03 18:20:00','2025-05-03 18:20:00', FALSE,       FALSE,          78);

-- 4) Post 테이블 더미 데이터 (ID 9999부터 시작, author_id는 위 member ID 참조)
INSERT INTO `post`
(`id`,   `title`,             `content`,                             `created_at`,         `modified_at`,         `status`,     `is_public`, `author_id`, `thumbnail_id`)
VALUES
    ( 9999, '첫 번째 게시글',      'MonStu 서비스에 오신 것을 환영합니다.',     '2025-05-01 08:00:00','2025-05-01 08:00:00','PUBLIC',  TRUE,        9999,        NULL),
    (10000,'React 사용 팁',      'useMemo와 useCallback으로 성능 최적화하기','2025-05-02 13:45:00','2025-05-02 14:00:00','PUBLIC',      FALSE,       10000,       NULL),
    (10001,'Spring Boot 보안 설정','JWT 기반 인증과 Spring Security 통합 방법','2025-05-03 16:10:00','2025-05-03 16:10:00','PUBLIC',  TRUE,        10001,       NULL);
