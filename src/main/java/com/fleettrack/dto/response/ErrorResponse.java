package com.fleettrack.dto.response;

import lombok.*;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private Instant timestamp;
    private Map<String, String> errors;
}