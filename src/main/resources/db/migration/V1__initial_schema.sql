-- Initial schema for orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_session_id ON orders(session_id);
CREATE INDEX idx_orders_email ON orders(email);
CREATE INDEX idx_orders_created_at ON orders(created_at);
