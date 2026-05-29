package com.fleettrack.controller;

import com.fleettrack.dto.request.VehicleRequest;
import com.fleettrack.dto.response.VehicleResponse;
import com.fleettrack.enums.VehicleStatus;
import com.fleettrack.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<Page<VehicleResponse>> getAll(
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String licensePlate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(vehicleService.getAll(
                status, yearFrom, yearTo,
                make, licensePlate, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(
            @Valid @RequestBody VehicleRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(
                vehicleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}