package com.fleettrack.service;

import com.fleettrack.dto.websocket.LocationRequest;
import com.fleettrack.dto.websocket.LocationUpdate;
import com.fleettrack.entity.Vehicle;
import com.fleettrack.exception.ResourceNotFoundException;
import com.fleettrack.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final VehicleRepository vehicleRepository;

    private final Map<Long, LocationUpdate> lastKnownLocations =
            new ConcurrentHashMap<>();

    public void updateLocation(LocationRequest request) {

        Vehicle vehicle = vehicleRepository
                .findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maşın", request.getVehicleId()));

        LocationUpdate update = LocationUpdate.builder()
                .vehicleId(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .speed(request.getSpeed())
                .timestamp(Instant.now())
                .build();

        lastKnownLocations.put(vehicle.getId(), update);

        messagingTemplate.convertAndSend(
                "/topic/location", update);

        messagingTemplate.convertAndSend(
                "/topic/location/" + vehicle.getId(), update);

        log.info("Location updated: vehicleId={}, lat={}, lng={}",
                vehicle.getId(),
                request.getLatitude(),
                request.getLongitude());
    }

    public Map<Long, LocationUpdate> getAllLastLocations() {
        return lastKnownLocations;
    }

    public LocationUpdate getLastLocation(Long vehicleId) {
        return lastKnownLocations.get(vehicleId);
    }
}