package com.fleettrack.mapper;

import com.fleettrack.dto.request.VehicleRequest;
import com.fleettrack.dto.response.VehicleResponse;
import com.fleettrack.entity.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignedDriver", ignore = true)
    @Mapping(target = "maintenanceRecords", ignore = true)
    Vehicle toEntity(VehicleRequest request);

    // Entity → Response
    @Mapping(
        target = "assignedDriverName",
        expression = "java(vehicle.getAssignedDriver() != null ? " +
                     "vehicle.getAssignedDriver().getFirstName() + ' ' + " +
                     "vehicle.getAssignedDriver().getLastName() : null)"
    )
    VehicleResponse toResponse(Vehicle vehicle);

    // Update — mövcud entity-ni yenilə
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignedDriver", ignore = true)
    @Mapping(target = "maintenanceRecords", ignore = true)
    void updateEntity(VehicleRequest request, @MappingTarget Vehicle vehicle);
}