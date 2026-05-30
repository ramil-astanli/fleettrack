package com.fleettrack.service;

import com.fleettrack.entity.Vehicle;
import com.fleettrack.entity.MaintenanceRecord;
import com.fleettrack.repository.VehicleRepository;
import com.fleettrack.repository.MaintenanceRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final VehicleRepository vehicleRepository;
    private final MaintenanceRepository maintenanceRepository;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public byte[] generateFleetReport() {

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            addTitle(document,
                    "FleetTrack — Fleet Status Hesabatı");
            addSubtitle(document,
                    "Tarix: " + LocalDate.now()
                            .format(FORMATTER));

            document.add(new Paragraph("\n"));

            addSectionTitle(document, "Avtomobillər");
            addVehiclesTable(document);

            document.add(new Paragraph("\n"));

            addSectionTitle(document,
                    "Gecikmiş Texniki Xidmətlər");
            addMaintenanceTable(document);

        } catch (Exception e) {
            log.error("PDF yaradılarkən xəta: {}", e.getMessage());
            throw new RuntimeException("PDF yaradıla bilmədi");
        }

        return outputStream.toByteArray();
    }

    private void addTitle(Document document, String title) {
        document.add(new Paragraph(title)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.DARK_GRAY));
    }

    private void addSubtitle(Document document, String text) {
        document.add(new Paragraph(text)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));
    }

    private void addSectionTitle(
            Document document, String title) {
        document.add(new Paragraph(title)
                .setFontSize(13)
                .setBold()
                .setFontColor(ColorConstants.BLUE));
    }

    private void addVehiclesTable(Document document) {

        List<Vehicle> vehicles =
                vehicleRepository.findAll(
                        PageRequest.of(0, 100))
                        .getContent();

        Table table = new Table(UnitValue.createPercentArray(
                new float[]{20, 20, 15, 20, 25}))
                .useAllAvailableWidth();

        addTableHeader(table, "Marka");
        addTableHeader(table, "Model");
        addTableHeader(table, "İl");
        addTableHeader(table, "Nişan");
        addTableHeader(table, "Status");

        vehicles.forEach(v -> {
            table.addCell(v.getMake());
            table.addCell(v.getModel());
            table.addCell(String.valueOf(v.getYear()));
            table.addCell(v.getLicensePlate());
            table.addCell(v.getStatus().name());
        });

        document.add(table);
    }

    private void addMaintenanceTable(Document document) {

        List<MaintenanceRecord> overdue =
                maintenanceRepository
                        .findByNextServiceDateBefore(
                                LocalDate.now());

        if (overdue.isEmpty()) {
            document.add(new Paragraph(
                    "Gecikmiş texniki xidmət yoxdur.")
                    .setFontColor(ColorConstants.GREEN));
            return;
        }

        Table table = new Table(UnitValue.createPercentArray(
                new float[]{25, 35, 20, 20}))
                .useAllAvailableWidth();

        addTableHeader(table, "Maşın");
        addTableHeader(table, "Təsvir");
        addTableHeader(table, "Son Xidmət");
        addTableHeader(table, "Növbəti Xidmət");

        overdue.forEach(r -> {
            table.addCell(
                    r.getVehicle().getLicensePlate());
            table.addCell(r.getDescription());
            table.addCell(r.getServiceDate()
                    .format(FORMATTER));
            table.addCell(r.getNextServiceDate()
                    .format(FORMATTER));
        });

        document.add(table);
    }

    private void addTableHeader(Table table, String text) {
        table.addHeaderCell(new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
    }
}