package com.abn.autoservice.parkingsystem.util;

import com.abn.autoservice.parkingsystem.entity.FineDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class PDFGeneratorTest {
    private PDFGenerator pdfGenerator;

    @BeforeEach
    void setup() {
        pdfGenerator = new PDFGenerator();
        List<String> columnNameList = Arrays.asList("Driver name", "Street name", "Observation date", "Fine amount");
        ReflectionTestUtils.setField(pdfGenerator, "pdfDir", "C:/pdf_fine_reports/");
        ReflectionTestUtils.setField(pdfGenerator, "reportFileName", "fine_report_test");
        ReflectionTestUtils.setField(pdfGenerator, "reportFileNameDateFormat", "dd_MMM_yyyy");
        ReflectionTestUtils.setField(pdfGenerator, "localDateFormat", "dd-MMM-yyyy");
        ReflectionTestUtils.setField(pdfGenerator, "noOfColumns", 4);
        ReflectionTestUtils.setField(pdfGenerator, "columnNames", columnNameList);
    }

    @Test
    void testGeneratePdfReport() {
        FineDetails fineDetails = new FineDetails();
        fineDetails.setDriverName("test");
        fineDetails.setStreetName("Jakarta");
        fineDetails.setLicensePlateNo("X-332-FD");
        fineDetails.setFineAmount(25.7);
        fineDetails.setObservationDate(Timestamp.from(Instant.now()));
        Assertions.assertDoesNotThrow(() -> pdfGenerator.generatePdfReport(fineDetails));
    }

    @Test
    void testGeneratePdfReportWithException() {
        try (MockedStatic<Files> filesMocked = mockStatic(Files.class)) {
            filesMocked.when(()-> Files.createDirectories(Mockito.any(Path.class))).thenThrow(IOException.class);
            FineDetails fineDetails = new FineDetails();
            fineDetails.setDriverName("test");
            fineDetails.setStreetName("Jakarta");
            fineDetails.setLicensePlateNo("X-332-FD");
            fineDetails.setFineAmount(25.7);
            fineDetails.setObservationDate(Timestamp.from(Instant.now()));
            Assertions.assertDoesNotThrow(() -> pdfGenerator.generatePdfReport(fineDetails));
        }
    }

}
