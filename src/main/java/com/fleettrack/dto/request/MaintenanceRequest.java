package com.fleettrack.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequest {

    @NotNull(message = "Maşın id boş ola bilməz")
    private Long vehicleId;

    @NotBlank(message = "Təsvir boş ola bilməz")
    private String description;

    @NotNull(message = "Xidmət tarixi boş ola bilməz")
    private LocalDate serviceDate;

    private LocalDate nextServiceDate;

    @DecimalMin(value = "0.0", message = "Xərc mənfi ola bilməz")
    private BigDecimal cost;
}