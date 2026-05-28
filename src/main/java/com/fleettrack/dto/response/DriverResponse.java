package com.fleettrack.dto.response;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String phone;
    private String email;
    private Long vehicleId;
    private String vehicleLicensePlate;
    private Instant createdAt;
}