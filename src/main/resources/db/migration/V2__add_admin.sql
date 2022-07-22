INSERT INTO users (id, archive,  email, name, password, role)
VALUES (1, false, 'mail@mail.com', 'admin', '$2a$10$INZWfmHHUDBrbKhwa8ubtee66v4rNuXAk4WkLLtlQjSITvGFeJNpS', 'ADMIN');

ALTER SEQUENCE user_seq RESTART WITH 2;