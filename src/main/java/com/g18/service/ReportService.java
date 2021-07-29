package com.g18.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.entity.Report;
import com.g18.entity.StudySet;
import com.g18.repository.ReportRepository;
import com.g18.repository.StudySetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {
    @Autowired
    private StudySetRepository studySetRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private ReportRepository reportRepository;

    @Transactional
    public String saveReport(Long ssID,ObjectNode json){
        Report report = new Report();
        StudySet ss = studySetRepository.findById(ssID).orElseThrow(()
                ->  new ExpressionException("Lỗi ! ko tìm thấy study set."));
        report.setStudySet(ss);
        report.setUser(authService.getCurrentUser());
        report.setContent(json.get("content").asText());
        reportRepository.save(report);
        return "Send report successfully";
    }
}
