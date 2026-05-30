package com.fleettrack.repository;

import com.fleettrack.entity.Vehicle;
import com.fleettrack.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository
        extends JpaRepository<Vehicle, Long>,
                JpaSpecificationExecutor<Vehicle> {

    Page<Vehicle> findByStatus(VehicleStatus status, Pageable pageable);

    Page<Vehicle> findByYearBetween(int from, int to, Pageable pageable);

    boolean existsByLicensePlate(String licensePlate);

    List<Vehicle> findAllByStatus(VehicleStatus status);
}