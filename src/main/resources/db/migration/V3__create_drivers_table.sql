CREATE TABLE drivers
(
    id             BIGSERIAL PRIMARY KEY,
    first_name     VARCHAR(50)  NOT NULL,
    last_name      VARCHAR(50)  NOT NULL,
    license_number VARCHAR(30)  NOT NULL UNIQUE,
    phone          VARCHAR(20),
    email          VARCHAR(100) UNIQUE,
    vehicle_id     BIGINT REFERENCES vehicles (id) ON DELETE SET NULL,
    created_at     TIMESTAMP DEFAULT NOW(),
    updated_at     TIMESTAMP DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_drivers_vehicle_id
    ON drivers (vehicle_id)
    WHERE vehicle_id IS NOT NULL;

CREATE INDEX idx_drivers_last_name
    ON drivers (last_name);