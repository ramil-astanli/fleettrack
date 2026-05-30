package com.fleettrack.pubsub;

import com.fleettrack.config.RedisPubSubConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publishMaintenanceAlert(String licensePlate, String date, String driverEmail) {
        // Məlumatı Map şəklində yığırıq (JSON-a çevriləcək)
        Map<String, String> payload = Map.of(
                "licensePlate", licensePlate,
                "date", date,
                "driverEmail",driverEmail,
                "type", "MAINTENANCE"
        );

        redisTemplate.convertAndSend(
                RedisPubSubConfig.MAINTENANCE_ALERT_CHANNEL,
                payload); // Artıq String yox, Object (JSON) göndəririk

        log.info("📢 JSON Pub/Sub yayımlandı: {}", payload, driverEmail);
    }

    public void publishVehicleOfflineAlert(String licensePlate) {
        Map<String, String> payload = Map.of(
                "licensePlate", licensePlate,
                "status", "OFFLINE",
                "type", "VEHICLE_STATUS"
        );

        redisTemplate.convertAndSend(
                RedisPubSubConfig.VEHICLE_OFFLINE_CHANNEL,
                payload);

        log.info("📢 JSON Pub/Sub yayımlandı: {}", payload);
    }
}