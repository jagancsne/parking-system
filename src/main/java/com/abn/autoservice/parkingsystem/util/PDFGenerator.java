package com.abn.autoservice.parkingsystem.util;

import com.abn.autoservice.parkingsystem.entity.FineDetails;
import com.abn.autoservice.parkingsystem.exception.ParkingSystemException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component("pdfGenerator")
public class PDFGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

    @Value("${parking.report.pdfDir}")
    private String pdfDir;

    @Value("${parking.report.reportFileName}")
    private String reportFileName;

    @Value("${parking.report.reportFileNameDateFormat}")
    private String reportFileNameDateFormat;

    @Value("${parking.report.localDateFormat}")
    private String localDateFormat;

    @Value("${parking.report.noOfColumns}")
    private int noOfColumns;

    @Value("${parking.report.columnNames}")
    private List<String> columnNames;

    private static final Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
    private static final Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
    private static final Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

    public void generatePdfReport(FineDetails fineInfo) {
        logger.debug("Enter - generatePdfReport method");
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(getPdfNameWithDate(fineInfo.getLicensePlateNo())));
            document.open();
            addDocTitle(document);
            createTable(document, noOfColumns, fineInfo);
            addFooter(document);
            document.close();
        } catch (FileNotFoundException | DocumentException | ParkingSystemException e) {
            logger.error("Error while generating report {} ", e.getMessage(), e.getCause());
        }
        logger.debug("Exit - generatePdfReport method");
    }

    private void addDocTitle(Document document) throws DocumentException {
        logger.trace("Adding document title");
        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(localDateFormat));
        Paragraph p1 = new Paragraph();
        leaveEmptyLine(p1, 1);
        p1.add(new Paragraph("Car parking fine report", COURIER));
        p1.setAlignment(Element.ALIGN_CENTER);
        leaveEmptyLine(p1, 1);
        p1.add(new Paragraph("Report generated on " + localDateString, COURIER_SMALL));
        document.add(p1);

    }

    private void createTable(Document document, int noOfColumns, FineDetails fineInfo) throws DocumentException {
        logger.trace("Creating tabular data");
        Paragraph paragraph = new Paragraph();
        leaveEmptyLine(paragraph, 3);
        document.add(paragraph);
        PdfPTable table = new PdfPTable(noOfColumns);

        for (int i = 0; i < noOfColumns; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.CYAN);
            table.addCell(cell);
        }

        table.setHeaderRows(1);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(fineInfo.getDriverName());
        table.addCell(fineInfo.getStreetName());
        table.addCell(fineInfo.getObservationDate().toLocalDateTime().toLocalDate().toString());
        table.addCell(fineInfo.getFineAmount().toString() + " Euros");
        document.add(table);
    }
    private void addFooter(Document document) throws DocumentException {
        logger.trace("Adding document footer");
        Paragraph p2 = new Paragraph();
        leaveEmptyLine(p2, 3);
        p2.setAlignment(Element.ALIGN_MIDDLE);
        p2.add(new Paragraph(
                "------------------------End of report------------------------",
                COURIER_SMALL_FOOTER));
        document.add(p2);
    }

    private static void leaveEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private String getPdfNameWithDate(String licenseNo) {

        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern(reportFileNameDateFormat));
        String pdfFilePath = pdfDir + reportFileName + "-" + localDateString + "-" + licenseNo + ".pdf";
        Path path = Paths.get(pdfDir);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new ParkingSystemException(e.getMessage(), e.getCause());
        }
        return pdfFilePath;
    }
}
