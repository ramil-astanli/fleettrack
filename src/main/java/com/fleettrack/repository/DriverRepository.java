package com.fleettrack.repository;

import com.fleettrack.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository
        extends JpaRepository<Driver, Long>,
                JpaSpecificationExecutor<Driver> {

    Optional<Driver> findByLicenseNumber(String licenseNumber);

    Page<Driver> findByLastNameContainingIgnoreCase(
        String lastName, Pageable pageable
    );

    boolean existsByLicenseNumber(String licenseNumber);

    Optional<Driver> findByVehicleId(Long vehicleId);
}