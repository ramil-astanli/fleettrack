package com.fleettrack.controller;

import com.fleettrack.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/fleet")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> generateFleetReport() {

        byte[] pdf = reportService.generateFleetReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("fleet-report.pdf")
                        .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}