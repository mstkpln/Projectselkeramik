-- Products table schema
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT true
);

CREATE INDEX idx_products_active ON products(active);

-- Insert initial product data
INSERT INTO products (name, price, image_url, stock, active) VALUES
('Ivy', 45.00, '/images/PXL_20260226_214519920~2.jpg', 10, true),
('Handmade Vase', 65.00, '/images/FullSizeRender_377.jpg', 5, true),
('Clay Pot', 55.00, '/images/FullSizeRender_548.jpg', 8, true),
('Artisan Plate', 35.00, '/images/FullSizeRender_777.webp', 15, true),
('Ceramic Cup', 25.00, '/images/FullSizeRender_95.jpg', 20, true),
('Sculptural Form3', 25.00, '/images/Screenshot2026-02-031223199.jpg', 12, true),
('Mini Vase_4', 35.00, '/images/PXL_20260226_214200859.jpg', 7, true),
('Mini Vase_5', 25.00, '/images/PXL_20260226_214448697~2.jpg', 9, true),
('Mini Vase_6', 25.00, '/images/PXL_20260226_214407125.jpg', 11, true),
('Mini Vase_7', 20.00, '/images/PXL_20260226_214334053.jpg', 14, true);
