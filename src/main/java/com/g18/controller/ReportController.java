package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping(value = "/api/report/{ssId}")
    public String createReport(@PathVariable("ssId") Long ssId,@RequestBody ObjectNode json){
        return reportService.saveReport(ssId,json);
    }

}
