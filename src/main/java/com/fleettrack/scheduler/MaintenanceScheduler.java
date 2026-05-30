package com.fleettrack.scheduler;

import com.fleettrack.entity.Driver;
import com.fleettrack.entity.MaintenanceRecord;
import com.fleettrack.pubsub.AlertPublisher;
import com.fleettrack.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceScheduler {

    private final MaintenanceRepository maintenanceRepository;
    private final AlertPublisher alertPublisher;

    @Scheduled(cron = "0 0 8 * * *")
    public void checkUpcomingMaintenance() {

        log.info("🔍 Yaxınlaşan texniki xidmətlər...");

        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<MaintenanceRecord> upcoming =
                maintenanceRepository
                        .findUpcomingMaintenance(
                                today, nextWeek);

        if (upcoming.isEmpty()) {
            log.info("✅ Yaxınlaşan texniki xidmət yoxdur");
            return;
        }

        log.info("📋 {} maşın", upcoming.size());

        upcoming.forEach(record -> {
            Driver driver = record.getVehicle().getAssignedDriver();

            // Əgər maşına təhkim olunmuş sürücü varsa, onun emailini götürürük
            String driverEmail = (driver != null) ? driver.getEmail() : "admin@fleettrack.com";

            alertPublisher.publishMaintenanceAlert(
                    record.getVehicle().getLicensePlate(),
                    record.getNextServiceDate().toString(),
                    driverEmail // Artıq 3-cü parametr olaraq ötürülür
            );
        });
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void checkOverdueMaintenance() {

        log.info("🔍 Gecikmiş texniki xidmətlər...");

        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<MaintenanceRecord> overdue =
                maintenanceRepository
                        .findByNextServiceDateBefore(yesterday);

        if (overdue.isEmpty()) {
            log.info("✅ Gecikmiş texniki xidmət yoxdur");
            return;
        }

        log.warn("⚠️ {} maşın gecikib", overdue.size());

        overdue.forEach(record -> {
            // Sürücü obyektini götürürük
            Driver driver = record.getVehicle().getAssignedDriver();

            // Əgər maşına təhkim olunmuş sürücü varsa, onun emailini götürürük
            String driverEmail = (driver != null) ? driver.getEmail() : "admin@fleettrack.com";

            alertPublisher.publishMaintenanceAlert(
                    record.getVehicle().getLicensePlate(),
                    record.getNextServiceDate().toString(),
                    driverEmail // Artıq 3-cü parametr olaraq ötürülür
            );
        });
    }
}