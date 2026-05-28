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

    // Status-a görə filter
    Page<Vehicle> findByStatus(VehicleStatus status, Pageable pageable);

    // İl aralığına görə filter
    Page<Vehicle> findByYearBetween(int from, int to, Pageable pageable);

    // License plate mövcuddurmu
    boolean existsByLicensePlate(String licensePlate);

    // Bütün aktiv maşınlar — cache üçün
    List<Vehicle> findAllByStatus(VehicleStatus status);
}