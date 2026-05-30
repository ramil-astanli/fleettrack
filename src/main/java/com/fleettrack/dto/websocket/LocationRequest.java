package com.fleettrack.dto.websocket;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {

    @NotNull(message = "Vehicle id boş ola bilməz")
    private Long vehicleId;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    private Double speed;
}