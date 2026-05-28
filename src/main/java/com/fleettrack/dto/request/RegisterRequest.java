package com.fleettrack.dto.request;

import com.fleettrack.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username boş ola bilməz")
    @Size(min = 3, max = 50, message = "Username 3-50 simvol olmalıdır")
    private String username;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @Size(min = 6, message = "Şifrə minimum 6 simvol olmalıdır")
    private String password;

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Email formatı yanlışdır")
    private String email;

    @NotNull(message = "Rol boş ola bilməz")
    private UserRole role;
}