package com.fleettrack.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleettrack.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Slf4j
@Service
public class AlertSubscriber {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public AlertSubscriber(SimpMessagingTemplate messagingTemplate,
                           NotificationService notificationService,
                           ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unchecked")
    public void onMaintenanceAlert(Object message) {
        try {
            Map<String, Object> data = parseMessage(message);
            if (data == null) return;

            String plate = String.valueOf(data.get("licensePlate"));
            String date = String.valueOf(data.get("date"));
            String driverEmail = String.valueOf(data.get("driverEmail"));

            log.info("🚀 [Maintenance] NotificationService çağırılır: Plate={}, Email={}", plate, driverEmail);

            messagingTemplate.convertAndSend("/topic/alerts", (Object) data);
            notificationService.sendMaintenanceReminder(plate, date, driverEmail);

            log.info("✅ {} nömrəli maşın üçün proses tamamlandı.", plate);
        } catch (Exception e) {
            log.error("❌ Maintenance emalında xəta: ", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void onVehicleOfflineAlert(Object message) {
        try {
            Map<String, Object> data = parseMessage(message);
            if (data == null) return;

            String plate = String.valueOf(data.get("licensePlate"));

            log.warn("📡 [Offline] NotificationService çağırılır: Plate={}", plate);

            messagingTemplate.convertAndSend("/topic/alerts", (Object) data);
            notificationService.sendVehicleOfflineAlert(plate);

            log.info("✅ {} nömrəli maşın üçün offline bildiriş göndərildi.", plate);
        } catch (Exception e) {
            log.error("❌ Offline emalında xəta: ", e);
        }
    }

    private Map<String, Object> parseMessage(Object message) throws Exception {
        if (message instanceof String) {
            return objectMapper.readValue((String) message, Map.class);
        } else if (message instanceof Map) {
            return (Map<String, Object>) message;
        }
        log.error("⚠️ Tanınmayan mesaj formatı: {}", message != null ? message.getClass().getName() : "null");
        return null;
    }
}