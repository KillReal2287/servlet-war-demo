CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO users (first_name, last_name, age, active)
SELECT first_name, last_name, age, active
FROM (
    VALUES
        ('Иван', 'Петров', 25, TRUE),
        ('Анна', 'Смирнова', 31, TRUE),
        ('Олег', 'Иванов', 42, FALSE),
        ('Мария', 'Кузнецова', 28, TRUE),
        ('Дмитрий', 'Соколов', 36, FALSE)
) AS initial_users(first_name, last_name, age, active)
WHERE NOT EXISTS (SELECT 1 FROM users);
