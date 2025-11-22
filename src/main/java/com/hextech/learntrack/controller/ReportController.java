package com.hextech.learntrack.controller;

import com.hextech.learntrack.model.User;
import com.hextech.learntrack.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Download CSV report of user's progress
     */
    @GetMapping("/csv")
    public ResponseEntity<String> downloadCSVReport(@AuthenticationPrincipal User user) {
        String csvContent = reportService.generateCSVReport(user);
        
        String filename = "progress_report_" + 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + 
                         ".csv";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvContent);
    }

    /**
     * Download detailed CSV report
     */
    @GetMapping("/csv/detailed")
    public ResponseEntity<String> downloadDetailedCSVReport(@AuthenticationPrincipal User user) {
        String csvContent = reportService.generateDetailedCSVReport(user);
        
        String filename = "detailed_progress_report_" + 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + 
                         ".csv";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvContent);
    }

    /**
     * Download PDF report (HTML format that can be printed to PDF)
     */
    @GetMapping("/pdf")
    public ResponseEntity<String> downloadPDFReport(@AuthenticationPrincipal User user) {
        String htmlContent = reportService.generatePDFContent(user);
        
        String filename = "progress_report_" + 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + 
                         ".html";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }

    /**
     * View PDF report in browser
     */
    @GetMapping("/view/pdf")
    public ResponseEntity<String> viewPDFReport(@AuthenticationPrincipal User user) {
        String htmlContent = reportService.generatePDFContent(user);
        
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }
}
