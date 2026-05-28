package com.fleettrack.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequest {

    @NotBlank(message = "Ad boş ola bilməz")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Soyad boş ola bilməz")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Vəsiqə nömrəsi boş ola bilməz")
    @Size(max = 30)
    private String licenseNumber;

    @Size(max = 20)
    private String phone;

    @Email(message = "Email formatı yanlışdır")
    private String email;

    private Long vehicleId;
}