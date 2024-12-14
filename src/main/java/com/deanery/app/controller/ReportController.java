package com.deanery.app.controller;

import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ReportController.REPORT_URL)
public class ReportController {
    public static final String REPORT_URL = "/report";
    private final ReportService reportService;

    @GetMapping("/educationPlan")
    public ResponseEntity<Void> computerReport(@RequestParam("eduPlan") UUID id, HttpServletResponse response) {
        reportService.exportComputerReport(response, id);
        return ResponseEntity.ok().build();
    }
}
