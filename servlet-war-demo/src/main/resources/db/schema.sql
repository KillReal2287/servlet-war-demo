CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO users (id, first_name, last_name, age, active)
VALUES
    (1, 'Иван', 'Петров', 25, TRUE),
    (2, 'Анна', 'Смирнова', 31, TRUE),
    (3, 'Олег', 'Иванов', 42, FALSE),
    (4, 'Мария', 'Кузнецова', 28, TRUE),
    (5, 'Дмитрий', 'Соколов', 36, FALSE)
ON CONFLICT (id) DO NOTHING;

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

CREATE TABLE IF NOT EXISTS posts (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    image_url TEXT,
    likes_count INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS post_paragraphs (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    paragraph_order INTEGER NOT NULL,
    text TEXT NOT NULL,
    CONSTRAINT post_paragraphs_post_id_order_unique UNIQUE (post_id, paragraph_order)
);

CREATE TABLE IF NOT EXISTS tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS post_tags (
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE IF NOT EXISTS post_comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    text TEXT NOT NULL
);
