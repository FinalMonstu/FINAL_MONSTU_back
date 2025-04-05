/* Sample Data*/

INSERT INTO member (id, email, password, nick_name, phone_number, created_at, updated_at, status, role, country_code)
VALUES
    (1, 'user1@example.com', '$2a$10$VwMC5BLYAuwX7H3AnouX/.aWCNpOqzHYwPb.G62et4MylKrBPbdHe', 'User One', '010-1111-1111', NOW(), NOW(), 'ACTIVE', 'MEMBER', 'KOR'),
    (2, 'user2@example.com', '$2a$10$VwMC5BLYAuwX7H3AnouX/.aWCNpOqzHYwPb.G62et4MylKrBPbdHe', 'User Two', '010-2222-2222', NOW(), NOW(), 'ACTIVE', 'ADMIN', 'USA'),
    (3, 'user3@example.com', '$2a$10$VwMC5BLYAuwX7H3AnouX/.aWCNpOqzHYwPb.G62et4MylKrBPbdHe', 'User Three', '010-3333-3333', NOW(), NOW(), 'INACTIVE', 'GUEST', 'JPN'),
    (4, 'user4@example.com', '$2a$10$VwMC5BLYAuwX7H3AnouX/.aWCNpOqzHYwPb.G62et4MylKrBPbdHe', 'User Four', '010-4444-4444', NOW(), NOW(), 'BANNED', 'MEMBER', 'FRA'),
    (5, 'user5@example.com', '$2a$10$VwMC5BLYAuwX7H3AnouX/.aWCNpOqzHYwPb.G62et4MylKrBPbdHe', 'User Five', '010-5555-5555', NOW(), NOW(), 'DELETED', 'MEMBER', 'CHN');



INSERT INTO post (id, author_id, title, content, is_public, created_at, modified_at, status, thumbnail_id)
VALUES
    (1, 1, 'Post Title 1', 'Content 1', true, NOW(), NOW(), 'PUBLIC', NULL),
    (2, 1, 'Post Title 2', 'Content 2', true, NOW(), NOW(), 'DRAFT', NULL),
    (3, 2, 'Post Title 3', 'Content 3', false, NOW(), NOW(), 'DELETED', NULL),
    (4, 3, 'Post Title 4', 'Content 4', true, NOW(), NOW(), 'PUBLIC', NULL),
    (5, 4, 'Post Title 5', 'Content 5', false, NOW(), NOW(), 'DRAFT', NULL);


INSERT INTO member_log (id, member_id, last_login, failed_login_time, failed_login_count)
VALUES
    (1, 1, NOW(), NULL, 0),
    (2, 2, NOW(), NOW(), 1),
    (3, 3, NOW(), NOW(), 2),
    (4, 4, NOW(), NULL, 0),
    (5, 5, NOW(), NOW(), 3);


INSERT INTO history (id, target, genre, language_code)
VALUES
    (1, 'hello', 'WORD', 'EN'),
    (2, '안녕하세요', 'WORD', 'KO'),
    (3, 'Bonjour tout le monde', 'SENTENCE', 'FR'),
    (4, 'こんにちは世界', 'SENTENCE', 'JA'),
    (5, 'Hola', 'WORD', 'ES');


INSERT INTO exception_log (id, level, line_num, created_at, class_name, exception_type, http_status, method_name, request_url, service, message)
VALUES
    (1, 0, 101, NOW(), 'AuthController', 'NullPointerException', '401 UNAUTHORIZED', 'login', '/api/login', 'auth', 'Null user object'),
    (2, 1, 57, NOW(), 'PostService', 'IllegalStateException', '500 INTERNAL_SERVER_ERROR', 'createPost', '/api/posts', 'post', 'Cannot create post'),
    (3, 2, 123, NOW(), 'MemberService', 'RuntimeException', '400 BAD_REQUEST', 'updateProfile', '/api/member', 'member', 'Invalid phone number'),
    (4, 1, 89, NOW(), 'ImageController', 'IOException', '500 INTERNAL_SERVER_ERROR', 'uploadImage', '/api/image', 'media', 'File not found'),
    (5, 0, 33, NOW(), 'TokenService', 'ExpiredJwtException', '403 FORBIDDEN', 'validateToken', '/api/token', 'auth', 'Token expired');


