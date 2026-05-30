package com.fleettrack.service;

import com.fleettrack.dto.request.MaintenanceRequest;
import com.fleettrack.dto.response.MaintenanceResponse;
import com.fleettrack.entity.MaintenanceRecord;
import com.fleettrack.entity.Vehicle;
import com.fleettrack.exception.*;
import com.fleettrack.mapper.MaintenanceMapper;
import com.fleettrack.repository.MaintenanceRepository;
import com.fleettrack.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceMapper maintenanceMapper;

    public Page<MaintenanceResponse> getAllByVehicle(
            Long vehicleId, Pageable pageable) {

        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Maşın", vehicleId);
        }

        return maintenanceRepository
                .findByVehicleId(vehicleId, pageable)
                .map(maintenanceMapper::toResponse);
    }

    public MaintenanceResponse getById(Long vehicleId, Long id) {

        return maintenanceRepository.findById(id)
                .filter(record ->
                    record.getVehicle().getId().equals(vehicleId))
                .map(maintenanceMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Texniki xidmət qeydi", id));
    }

    @Transactional
    public MaintenanceResponse create(
            Long vehicleId, MaintenanceRequest request) {

        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maşın", vehicleId));

        MaintenanceRecord record =
                maintenanceMapper.toEntity(request);
        record.setVehicle(vehicle);

        return maintenanceMapper.toResponse(
                maintenanceRepository.save(record));
    }

    @Transactional
    public void delete(Long vehicleId, Long id) {

        MaintenanceRecord record = maintenanceRepository
                .findById(id)
                .filter(r ->
                    r.getVehicle().getId().equals(vehicleId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Texniki xidmət qeydi", id));

        maintenanceRepository.delete(record);
    }
}