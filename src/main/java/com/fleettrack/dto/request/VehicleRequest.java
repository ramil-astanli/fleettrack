package com.fleettrack.dto.request;

import com.fleettrack.enums.VehicleStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {

    @NotBlank(message = "Make boş ola bilməz")
    @Size(max = 50)
    private String make;

    @NotBlank(message = "Model boş ola bilməz")
    @Size(max = 50)
    private String model;

    @NotNull(message = "İl boş ola bilməz")
    @Min(value = 1900, message = "İl 1900-dən böyük olmalıdır")
    @Max(value = 2100, message = "İl 2100-dən kiçik olmalıdır")
    private Integer year;

    @NotBlank(message = "Qeydiyyat nişanı boş ola bilməz")
    @Size(max = 20)
    private String licensePlate;

    private VehicleStatus status = VehicleStatus.ACTIVE;
}