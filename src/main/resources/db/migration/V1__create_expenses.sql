CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    date DATE NOT NULL
);