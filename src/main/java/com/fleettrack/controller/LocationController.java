package com.fleettrack.controller;

import com.fleettrack.dto.websocket.LocationRequest;
import com.fleettrack.dto.websocket.LocationUpdate;
import com.fleettrack.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @MessageMapping("/location")
    public void receiveLocation(
            @Valid LocationRequest request) {
        locationService.updateLocation(request);
    }

    @GetMapping("/api/v1/locations")
    @PreAuthorize("hasAnyRole('ADMIN', 'FLEET_MANAGER')")
    public ResponseEntity<Map<Long, LocationUpdate>> getAllLocations() {
        return ResponseEntity.ok(
                locationService.getAllLastLocations());
    }

    @GetMapping("/api/v1/locations/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FLEET_MANAGER')")
    public ResponseEntity<LocationUpdate> getLocation(
            @PathVariable Long vehicleId) {

        LocationUpdate location =
                locationService.getLastLocation(vehicleId);

        if (location == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(location);
    }
}