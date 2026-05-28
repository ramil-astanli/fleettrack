INSERT INTO users (username, password, email, role)
VALUES ('admin',
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
        'admin@fleettrack.com',
        'ADMIN'),
       ('manager',
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
        'manager@fleettrack.com',
        'FLEET_MANAGER');

INSERT INTO vehicles (make, model, year, license_plate, status)
VALUES ('Toyota', 'Camry', 2022, '10-AA-001', 'ACTIVE'),
       ('Ford', 'Transit', 2021, '10-BB-002', 'ACTIVE'),
       ('Mercedes', 'Sprinter', 2023, '10-CC-003', 'MAINTENANCE'),
       ('Volkswagen', 'Crafter', 2020, '10-DD-004', 'INACTIVE');

INSERT INTO drivers (first_name, last_name, license_number, phone, email, vehicle_id)
VALUES ('Əli', 'Həsənov', 'AZ-001234', '+994501234567', 'ali@fleettrack.com', 1),
       ('Vüsal', 'Məmmədov', 'AZ-005678', '+994552345678', 'vusal@fleettrack.com', 2);

INSERT INTO maintenance_records (vehicle_id, description, service_date, next_service_date, cost)
VALUES (1, 'Yağ dəyişimi', '2024-01-15', '2024-07-15', 150.00),
       (2, 'Əyləc yoxlaması', '2024-02-20', '2024-08-20', 200.00),
       (3, 'Mühərrik təmiri', '2024-03-10', '2024-09-10', 850.00);