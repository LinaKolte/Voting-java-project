
package com.example.demo.student.controller;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.student.model.Voter;
import com.example.demo.student.repository.VoterRepositorye;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
@RequestMapping("/api/voter")
@CrossOrigin(origins = "*")
public class VoterController {

    private static final Logger log = LoggerFactory.getLogger(VoterController.class);

    private final VoterRepositorye voterRepositorye;
    @Autowired
    public VoterController(VoterRepositorye voterRepositorye) {
        this.voterRepositorye = voterRepositorye;
    }
    
    private byte[] generatePdfCardInMemory(Voter voter) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4); // Portrait layout
    
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
    
            // Fonts
            Font headingFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.WHITE);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font subHeaderFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
    
            // ===== Header =====
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
    
            PdfPCell headingCell = new PdfPCell(new Phrase("भारत निवडणूक आयोग", headingFont));
            headingCell.setBackgroundColor(new BaseColor(0, 51, 102));
            headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headingCell.setFixedHeight(35f);
            headingCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(headingCell);
    
            PdfPCell titleCell = new PdfPCell(new Phrase("मतदार ओळखपत्र Voter ID Card", titleFont));
            titleCell.setBackgroundColor(new BaseColor(0, 51, 102));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setFixedHeight(30f);
            titleCell.setBorder(PdfPCell.NO_BORDER);
            headerTable.addCell(titleCell);
    
            document.add(headerTable);
            document.add(new Paragraph(" ")); // Spacer
    
            // ===== Main Layout Table =====
            PdfPTable mainTable = new PdfPTable(2);
            mainTable.setWidthPercentage(100);
            mainTable.setWidths(new float[]{70, 30}); // Left: Info, Right: Photo+Signature
    
            // ----- LEFT COLUMN (Info) -----
            PdfPTable infoTable = new PdfPTable(1);
            infoTable.setWidthPercentage(100);
    
            infoTable.addCell(createNoBorderCell("Full Name  पूर्ण नाव: " + voter.getFirstName() + " " + voter.getMiddleName() + " " + voter.getLastName(), normalFont));
            infoTable.addCell(createNoBorderCell("Aadhaar No आधार क्रमांक: " + voter.getaadharno(), normalFont));
            infoTable.addCell(createNoBorderCell("DOB  जन्मतारीख: " + voter.getDob().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), normalFont));
            infoTable.addCell(createNoBorderCell("Voter ID मतदार आयडी: " + voter.getVoterId(), normalFont));
            infoTable.addCell(createNoBorderCell("Address पत्ता: " + voter.getAddress(), normalFont));
            infoTable.addCell(createNoBorderCell("City  शहर: " + voter.getCity() + ", " + voter.getState() + ", " + voter.getPostal(), normalFont));
            infoTable.addCell(createNoBorderCell("Citizen नागरिक: " + (voter.isCitizen() ? "Yes  होय" : "No नाही"), normalFont));
            infoTable.addCell(createNoBorderCell("Age Eligible  वय पात्र: " + (voter.isAgeEligible() ? "Yes  होय" : "No  नाही"), normalFont));
    
            PdfPCell infoCell = new PdfPCell(infoTable);
            infoCell.setBorder(PdfPCell.NO_BORDER);
            mainTable.addCell(infoCell);
    
            // ----- RIGHT COLUMN (Photo + Signature) -----
            PdfPTable rightTable = new PdfPTable(1);
            rightTable.setWidthPercentage(100);
    
            // --- Photo ---
            PdfPCell photoCell = new PdfPCell();
            photoCell.setBorder(PdfPCell.NO_BORDER);
            photoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (voter.getPhotoData() != null) {
                Image photo = Image.getInstance(voter.getPhotoData());
                photo.scaleToFit(100, 100);
                photoCell.addElement(photo);
            } else {
                photoCell.addElement(new Paragraph("No Photo / फोटो नाही", normalFont));
            }
            rightTable.addCell(photoCell);
    
            // --- Signature ---
            PdfPCell signCell = new PdfPCell();
            signCell.setBorder(PdfPCell.NO_BORDER);
            signCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (voter.getSignatureData() != null) {
                signCell.addElement(new Paragraph("Signature / स्वाक्षरी:", normalFont));
                Image signature = Image.getInstance(voter.getSignatureData());
                signature.scaleToFit(100, 50);
                signCell.addElement(signature);
            } else {
                signCell.addElement(new Paragraph("No Signature / स्वाक्षरी नाही", normalFont));
            }
            rightTable.addCell(signCell);
    
            PdfPCell rightCol = new PdfPCell(rightTable);
            rightCol.setBorder(PdfPCell.NO_BORDER);
            mainTable.addCell(rightCol);
    
            document.add(mainTable);
            document.add(new Paragraph(" ")); // Spacer
    
            // ===== Footer =====
            Paragraph footer = new Paragraph("हे दस्तऐवज संगणकाद्वारे तयार केलेले आहे. कृपया त्याची सत्यता तपासा.\nThis document is system-generated. Please verify its authenticity.", subHeaderFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
    
        } catch (Exception e) {
            log.error("Error generating PDF document", e);
            throw new Exception("Error while generating PDF", e);
        } finally {
            document.close();
        }
    
        return outputStream.toByteArray();
    }
    
    // Helper
    private PdfPCell createNoBorderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    
    // PDF Generation in Memory (ByteArrayOutputStream)
    
    // Endpoint to Generate the Voter ID Card PDF
    
@PostMapping("/generate-id")
public ResponseEntity<?> generateVoterIdCard(@RequestBody Map<String, String> requestData) {
    try {
        String aadharno = requestData.get("aadharno").trim();
        String dobStr = requestData.get("dob").trim();
        String email = requestData.get("email").trim();

        // Parse DOB string into LocalDate
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobStr); // Expected format: yyyy-MM-dd
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format for DOB. Expected format: yyyy-MM-dd");
        }

        // Finding the Voter using Aadhaar number, DOB, and Email
        Optional<Voter> voterOpt = voterRepositorye.findByAadharnoAndDobAndEmail(aadharno, dob, email);
        if (voterOpt.isEmpty()) {
            log.warn("Voter with Aadhaar " + aadharno + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voter not found");
        }

        Voter voter = voterOpt.get();

        // Generate Voter ID if not present
        if (voter.getVoterId() == null || voter.getVoterId().isEmpty()) {
            String voterId = "VID" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            voter.setVoterId(voterId);
            voterRepositorye.save(voter);
            log.info("Generated new Voter ID: " + voterId + " for voter " + voter.getaadharno());
        }

        // Generate the PDF in memory
        byte[] pdfData = generatePdfCardInMemory(voter); // Make sure you have this method implemented

        ByteArrayResource resource = new ByteArrayResource(pdfData);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + voter.getaadharno() + "_voter_id.pdf")
                .body(resource);

    } catch (Exception e) {
        log.error("Error occurred while generating Voter ID card", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate Voter ID card");
    }
}
}