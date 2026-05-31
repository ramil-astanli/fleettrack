package com.fleettrack.controller;

import com.fleettrack.dto.request.MaintenanceRequest;
import com.fleettrack.dto.response.MaintenanceResponse;
import com.fleettrack.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles/{vehicleId}/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FLEET_MANAGER')")
    public ResponseEntity<Page<MaintenanceResponse>> getAll(
            @PathVariable Long vehicleId,
            @PageableDefault(
                    size = 10,
                    sort = "serviceDate",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                maintenanceService.getAllByVehicle(
                        vehicleId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FLEET_MANAGER')")
    public ResponseEntity<MaintenanceResponse> getById(
            @PathVariable Long vehicleId,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                maintenanceService.getById(vehicleId, id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FLEET_MANAGER')")
    public ResponseEntity<MaintenanceResponse> create(
            @PathVariable Long vehicleId,
            @Valid @RequestBody MaintenanceRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(maintenanceService.create(
                        vehicleId, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long vehicleId,
            @PathVariable Long id) {
        maintenanceService.delete(vehicleId, id);
        return ResponseEntity.noContent().build();
    }
}