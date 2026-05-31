package com.fleettrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendMaintenanceReminder(String licensePlate, String nextServiceDate, String driverEmail) {
        log.info("🔔 [SİSTEM BİLDİRİŞİ] - {} nömrəli maşın üçün xidmət yaxınlaşır. Planlaşdırılan tarix: {}",
                licensePlate, nextServiceDate);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(driverEmail); // Real alıcı emaili bura yazılmalıdır
            message.setSubject("🛠️ Texniki Baxış Xatırlatması: " + licensePlate);
            message.setText(String.format(
                    "Hörmətli Menecer,\n\n%s nömrəli nəqliyyat vasitəsinin növbəti xidmət vaxtı yaxınlaşır.\n" +
                            "Planlaşdırılan tarix: %s\n\nZəhmət olmasa vaxtında yoxlanış təyin edin.",
                    licensePlate, nextServiceDate));

            mailSender.send(message);
            log.info("✅ Texniki xidmət emaili uğurla göndərildi: {}", licensePlate);
        } catch (Exception e) {
            log.error("❌ Email göndərilərkən xəta: {}", e.getMessage());
        }
    }

    public void sendVehicleOfflineAlert(String licensePlate) {
        log.warn("📡 [KRİTİK XƏBƏRDARLIQ] - {} nömrəli maşınla əlaqə KƏSİLDİ!", licensePlate);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo("support@fleettrack.com");
            message.setSubject("⚠️ KRİTİK: Maşın Offline oldu!");
            message.setText(licensePlate + " nömrəli nəqliyyat vasitəsi ilə əlaqə kəsilib. Təcili yoxlanış lazımdır.");

            mailSender.send(message);
            log.info("✅ Offline xəbərdarlıq emaili göndərildi.");
        } catch (Exception e) {
            log.error("❌ Offline emaili göndərilərkən xəta: {}", e.getMessage());
        }
    }

    @Async("taskExecutor")
    public void sendOverdueMaintenanceAlert(String licensePlate, String overdueDate) {
        log.error("🚨 [GECİKMƏ] - {} nömrəli maşının texniki baxış vaxtı keçib! Son tarix idi: {}",
                licensePlate, overdueDate);
    }
}