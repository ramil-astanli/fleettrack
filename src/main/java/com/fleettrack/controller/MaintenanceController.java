package com.fleettrack.controller;

import com.fleettrack.dto.request.MaintenanceRequest;
import com.fleettrack.dto.response.MaintenanceResponse;
import com.fleettrack.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles/{vehicleId}/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<Page<MaintenanceResponse>> getAll(
            @PathVariable Long vehicleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "serviceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                maintenanceService.getAllByVehicle(
                        vehicleId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> getById(
            @PathVariable Long vehicleId,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                maintenanceService.getById(vehicleId, id));
    }

    @PostMapping
    public ResponseEntity<MaintenanceResponse> create(
            @PathVariable Long vehicleId,
            @Valid @RequestBody MaintenanceRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(maintenanceService.create(vehicleId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long vehicleId,
            @PathVariable Long id) {
        maintenanceService.delete(vehicleId, id);
        return ResponseEntity.noContent().build();
    }
}