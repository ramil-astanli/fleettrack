package com.fleettrack.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponse {

    private Long id;
    private Long vehicleId;
    private String vehicleLicensePlate;
    private String description;
    private LocalDate serviceDate;
    private LocalDate nextServiceDate;
    private BigDecimal cost;
    private Instant createdAt;
}