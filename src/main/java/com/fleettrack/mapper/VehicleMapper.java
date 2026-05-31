package com.fleettrack.mapper;

import com.fleettrack.dto.request.VehicleRequest;
import com.fleettrack.dto.response.VehicleResponse;
import com.fleettrack.entity.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignedDriver", ignore = true)
    @Mapping(target = "maintenanceRecords", ignore = true)
    Vehicle toEntity(VehicleRequest request);

    @Mapping(
        target = "assignedDriverName",
        expression = "java(vehicle.getAssignedDriver() != null ? " +
                     "vehicle.getAssignedDriver().getFirstName() + ' ' + " +
                     "vehicle.getAssignedDriver().getLastName() : null)"
    )
    VehicleResponse toResponse(Vehicle vehicle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignedDriver", ignore = true)
    @Mapping(target = "maintenanceRecords", ignore = true)
    void updateEntity(VehicleRequest request, @MappingTarget Vehicle vehicle);
}