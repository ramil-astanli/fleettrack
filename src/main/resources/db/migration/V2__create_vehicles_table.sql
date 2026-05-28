CREATE TYPE vehicle_status AS ENUM(
    'ACTIVE',
    'MAINTENANCE',
    'INACTIVE'
);

CREATE TABLE vehicles
(
    id            BIGSERIAL PRIMARY KEY,
    make          VARCHAR(50)    NOT NULL,
    model         VARCHAR(50)    NOT NULL,
    year          INTEGER        NOT NULL,
    license_plate VARCHAR(20)    NOT NULL UNIQUE,
    status        vehicle_status NOT NULL DEFAULT 'ACTIVE',
    created_at    TIMESTAMP DEFAULT NOW(),
    updated_at    TIMESTAMP DEFAULT NOW(),
    created_by    VARCHAR(50),
    updated_by    VARCHAR(50)
);
CREATE INDEX idx_vehicles_status
    ON vehicles (status);

CREATE INDEX idx_vehicles_status_year
    ON vehicles(status, year);
