package com.fleettrack.service;

import com.fleettrack.dto.request.VehicleRequest;
import com.fleettrack.dto.response.VehicleResponse;
import com.fleettrack.entity.Vehicle;
import com.fleettrack.enums.VehicleStatus;
import com.fleettrack.exception.BusinessException;
import com.fleettrack.exception.ResourceNotFoundException;
import com.fleettrack.mapper.VehicleMapper;
import com.fleettrack.repository.VehicleRepository;
import com.fleettrack.specification.VehicleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public Page<VehicleResponse> getAll(
            VehicleStatus status,
            Integer yearFrom,
            Integer yearTo,
            String make,
            String licensePlate,
            Pageable pageable) {

        Specification<Vehicle> spec = Specification
                .where(VehicleSpecification.hasStatus(status))
                .and(VehicleSpecification.yearBetween(yearFrom, yearTo))
                .and(VehicleSpecification.hasMake(make))
                .and(VehicleSpecification.hasLicensePlate(licensePlate));

        return vehicleRepository.findAll(spec, pageable)
                .map(vehicleMapper :: toResponse);
    }

    public VehicleResponse getById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Car", id));
    }

    @Transactional
    public VehicleResponse create(VehicleRequest request) {
        if(vehicleRepository.existsByLicensePlate(
                request.getLicensePlate())) {
            throw new BusinessException(
                    "Bu qeydiyyat nisani artiq movcuddur: "
                    + request.getLicensePlate());
        }

        Vehicle vehicle = vehicleMapper.toEntity(request);
        return vehicleMapper.toResponse(
                vehicleRepository.save(vehicle));
    }

    @Transactional
    public VehicleResponse update(Long id, VehicleRequest request) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maşın", id));

        // Başqa maşında eyni nişan varsa xəta
        if (!vehicle.getLicensePlate()
                .equals(request.getLicensePlate()) &&
                vehicleRepository.existsByLicensePlate(
                        request.getLicensePlate())) {
            throw new BusinessException(
                    "Bu qeydiyyat nişanı artıq mövcuddur: "
                            + request.getLicensePlate());
        }

        vehicleMapper.updateEntity(request, vehicle);
        return vehicleMapper.toResponse(
                vehicleRepository.save(vehicle));
    }

    // Sil
    @Transactional
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maşın", id);
        }
        vehicleRepository.deleteById(id);
    }
}
