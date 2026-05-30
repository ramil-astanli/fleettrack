package com.fleettrack.dto.response;

import com.fleettrack.enums.VehicleStatus;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse implements Serializable {

    private Long id;
    private String make;
    private String model;
    private Integer year;
    private String licensePlate;
    private VehicleStatus status;
    private String assignedDriverName;
    private Instant createdAt;
    private Instant updatedAt;
}