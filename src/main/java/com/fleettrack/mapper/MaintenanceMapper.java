package com.fleettrack.mapper;

import com.fleettrack.dto.request.MaintenanceRequest;
import com.fleettrack.dto.response.MaintenanceResponse;
import com.fleettrack.entity.MaintenanceRecord;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    MaintenanceRecord toEntity(MaintenanceRequest request);

    @Mapping(
        target = "vehicleId",
        expression = "java(record.getVehicle().getId())"
    )
    @Mapping(
        target = "vehicleLicensePlate",
        expression = "java(record.getVehicle().getLicensePlate())"
    )
    MaintenanceResponse toResponse(MaintenanceRecord record);
}