CREATE TABLE maintenance_records
(
    id                BIGSERIAL PRIMARY KEY,
    vehicle_id        BIGINT         NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
    description       TEXT           NOT NULL,
    service_date      DATE           NOT NULL,
    next_service_date DATE,
    cost              DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    created_at        TIMESTAMP DEFAULT NOW(),
    created_by        VARCHAR(50)
);

CREATE INDEX idx_maintenance_vehicle_id
    ON maintenance_records (vehicle_id);

CREATE INDEX idx_maintenance_next_service
    ON maintenance_records (next_service_date);