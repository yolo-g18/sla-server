package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.ReportDto;
import com.g18.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping(value = "/api/report/{ssId}")
    public String createReport(@PathVariable("ssId") Long ssId,@RequestBody ObjectNode json){
        return reportService.saveReport(ssId,json);
    }
    @GetMapping("api/report/all")
    public Page<ReportDto> getAllReports(Pageable pageable) {
        return reportService.getAllReport(pageable);
    }
    @GetMapping("api/report/ss")
    public Page<ObjectNode> getSSHasReport(Pageable pageable) throws IOException {
        List<ObjectNode> nodes = reportService.getSSReport();
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > nodes.size() ? nodes.size()
                : (start + pageable.getPageSize()));

        return new PageImpl<ObjectNode>(nodes.subList(start, end), pageable, nodes.size());

    }



}
