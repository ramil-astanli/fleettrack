package com.fleettrack.repository;

import com.fleettrack.entity.MaintenanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRepository
        extends JpaRepository<MaintenanceRecord, Long> {

    // Maşının bütün qeydləri
    Page<MaintenanceRecord> findByVehicleId(
        Long vehicleId, Pageable pageable
    );

    // Növbəti xidməti yaxınlaşanlar — Scheduler üçün
    @Query("SELECT m FROM MaintenanceRecord m " +
           "WHERE m.nextServiceDate BETWEEN :from AND :to")
    List<MaintenanceRecord> findUpcomingMaintenance(
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );

    // Gecikmiş xidmətlər
    List<MaintenanceRecord> findByNextServiceDateBefore(LocalDate date);
}