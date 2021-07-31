package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.ReportDto;
import com.g18.entity.Room;
import com.g18.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.g18.entity.Report;
import com.g18.entity.StudySet;
import com.g18.repository.ReportRepository;
import com.g18.repository.StudySetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private StudySetRepository studySetRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Page<ReportDto> getAllReport(Pageable pageable){
        Page<Report> reports = reportRepository.findAll(pageable);
        int totalElements = (int) reports.getTotalElements();
        return new PageImpl<ReportDto>(
                reports.stream().map(report -> new ReportDto(
                            report.getId(),
                            report.getStudySet().getId(),
                            report.getStudySet().getTitle(),
                            accountRepository.findUserNameByUserId(report.getUser().getId()),
                            report.getContent()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }


    @Transactional
    public String saveReport(Long ssID,ObjectNode json){
        Report report = new Report();
        StudySet ss = studySetRepository.findById(ssID).orElseThrow(()
                ->  new ExpressionException("Lỗi ! ko tìm thấy Study set."));
        report.setStudySet(ss);
        report.setUser(authService.getCurrentUser());
        report.setContent(json.get("content").asText());
        report.setCreatedTime(Instant.now());
        reportRepository.save(report);
        return "Send report successfully";
    }
    @Transactional
    public List<ObjectNode> getSSReport(){
        List<ObjectNode> objectNodeList = new ArrayList<>();
        List<StudySet> listSSHasReport = studySetRepository.findAllSSHasReport();

        if(listSSHasReport.isEmpty()){
            return objectNodeList;
        }
        // helper create objectnode
        ObjectMapper mapper;

        // load all room to json list
        for (StudySet ss: listSSHasReport) {
            mapper =  new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("SSID",ss.getId());
            json.put("title",ss.getTitle());
            json.put("creator",accountRepository.findUserNameByUserId(ss.getCreator().getId()));
            json.put("numberOfReport",reportRepository.numberOfReportSS(ss.getId()));
            objectNodeList.add(json);
        }
        return objectNodeList;
    }


}
