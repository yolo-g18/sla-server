package com.g18.controller;

import com.g18.dto.ReportDto;
import com.g18.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/reports")
    public Page<ReportDto> getAllReports(Pageable pageable) {
        return reportService.getAllReport(pageable);
    }

}
