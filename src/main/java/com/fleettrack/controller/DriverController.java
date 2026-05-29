package com.fleettrack.controller;

import com.fleettrack.dto.request.DriverRequest;
import com.fleettrack.dto.response.DriverResponse;
import com.fleettrack.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<Page<DriverResponse>> getAll(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean hasVehicle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                driverService.getAll(lastName, hasVehicle, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(driverService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DriverResponse> create(
            @Valid @RequestBody DriverRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(driverService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DriverRequest request) {
        return ResponseEntity.ok(
                driverService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}