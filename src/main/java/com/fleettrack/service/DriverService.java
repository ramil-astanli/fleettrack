package com.fleettrack.service;

import com.fleettrack.dto.request.DriverRequest;
import com.fleettrack.dto.response.DriverResponse;
import com.fleettrack.entity.Driver;
import com.fleettrack.entity.Vehicle;
import com.fleettrack.exception.*;
import com.fleettrack.mapper.DriverMapper;
import com.fleettrack.repository.DriverRepository;
import com.fleettrack.repository.VehicleRepository;
import com.fleettrack.specification.DriverSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverMapper driverMapper;

    public Page<DriverResponse> getAll(
            String lastName,
            Boolean hasVehicle,
            Pageable pageable) {

        Specification<Driver> spec = Specification
                .where(DriverSpecification.hasLastName(lastName));

        if (Boolean.TRUE.equals(hasVehicle)) {
            spec = spec.and(
                DriverSpecification.hasVehicleAssigned());
        } else if (Boolean.FALSE.equals(hasVehicle)) {
            spec = spec.and(
                DriverSpecification.hasNoVehicle());
        }

        return driverRepository.findAll(spec, pageable)
                .map(driverMapper::toResponse);
    }

    @Cacheable(value = "vehicle", key = "#id")
    public DriverResponse getById(Long id) {
        return driverRepository.findById(id)
                .map(driverMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sürücü", id));
    }

    @Transactional
    @CacheEvict(value = "vehicle", allEntries = true)
    public DriverResponse create(DriverRequest request) {

        if (driverRepository.existsByLicenseNumber(
                request.getLicenseNumber())) {
            throw new BusinessException(
                "Bu vəsiqə nömrəsi artıq mövcuddur: "
                + request.getLicenseNumber());
        }

        Driver driver = driverMapper.toEntity(request);

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository
                    .findById(request.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Maşın", request.getVehicleId()));

            // Maşında artıq sürücü varsa
            if (driverRepository.findByVehicleId(
                    vehicle.getId()).isPresent()) {
                throw new BusinessException(
                    "Bu maşında artıq sürücü var");
            }

            driver.setVehicle(vehicle);
        }

        return driverMapper.toResponse(
                driverRepository.save(driver));
    }

    @Transactional
    @CacheEvict(value = "vehicle", key = "#id")
    public DriverResponse update(Long id, DriverRequest request) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sürücü", id));

        driverMapper.updateEntity(request, driver);

        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository
                    .findById(request.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Maşın", request.getVehicleId()));
            driver.setVehicle(vehicle);
        } else {
            driver.setVehicle(null);
        }

        return driverMapper.toResponse(
                driverRepository.save(driver));
    }

    @Transactional
    @CacheEvict(value = "vehicle", key = "#id")
    public void delete(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sürücü", id);
        }
        driverRepository.deleteById(id);
    }
}