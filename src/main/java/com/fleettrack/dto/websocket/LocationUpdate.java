package com.fleettrack.dto.websocket;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationUpdate {

    private Long vehicleId;
    private String licensePlate;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private Instant timestamp;
}