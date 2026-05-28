package com.fleettrack.mapper;

import com.fleettrack.dto.request.DriverRequest;
import com.fleettrack.dto.response.DriverResponse;
import com.fleettrack.entity.Driver;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    Driver toEntity(DriverRequest request);

    @Mapping(
        target = "vehicleId",
        expression = "java(driver.getVehicle() != null ? " +
                     "driver.getVehicle().getId() : null)"
    )
    @Mapping(
        target = "vehicleLicensePlate",
        expression = "java(driver.getVehicle() != null ? " +
                     "driver.getVehicle().getLicensePlate() : null)"
    )
    DriverResponse toResponse(Driver driver);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    void updateEntity(DriverRequest request, @MappingTarget Driver driver);
}