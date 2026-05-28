package com.fleettrack.entity;

import com.fleettrack.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String make;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "vehicle_status")
    private VehicleStatus status = VehicleStatus.ACTIVE;

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Driver assignedDriver;

    // ← BU ƏLAVƏ EDİLDİ
    @OneToMany(mappedBy = "vehicle",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @Builder.Default
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();
}