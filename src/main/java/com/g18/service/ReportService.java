package com.g18.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.ReportDto;
import com.g18.repository.AccountRepository;
import com.g18.repository.UserRepository;
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
import java.util.*;
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
    @Autowired
    private UserRepository userRepository;

    public Page<ReportDto> getAllReport(Pageable pageable){
        Page<Report> reports = reportRepository.findAll(pageable);
        int totalElements = (int) reports.getTotalElements();
        return new PageImpl<ReportDto>(
                reports.stream().map(report -> new ReportDto(
                            report.getId(),
                            report.getStudySet().getId(),
                            report.getStudySet().getTitle(),
                            accountRepository.findUserNameByUserId(report.getReporter().getId()),
                            report.getContent(),
                            userRepository.findById(report.getReporter().getId()).get().getAvatar(),
                            String.valueOf(report.getCreatedTime()),
                            report.isChecked()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }
    //get all reports which property public is true or false
    public Page<ReportDto> getReportFilterByIsPublic(Pageable pageable,boolean type){
        List<ReportDto> reportDtoList = new ArrayList<>();
        Page<Report> reportList;
        if(type==true){
            reportList = reportRepository.findByIsCheckedTrue(pageable);
            int totalElements = (int) reportList.getTotalElements();
            return new PageImpl<ReportDto>(
                    reportList.stream().map(report -> new ReportDto(
                                    report.getId(),
                                    report.getStudySet().getId(),
                                    report.getStudySet().getTitle(),
                                    accountRepository.findUserNameByUserId(report.getReporter().getId()),
                                    report.getContent(),
                                    userRepository.findById(report.getReporter().getId()).get().getAvatar(),
                                    String.valueOf(report.getCreatedTime()),
                                    report.isChecked()
                            )
                    ).collect(Collectors.toList()), pageable, totalElements);

        }else {
            reportList = reportRepository.findByIsCheckedFalse(pageable);
            int totalElements = (int) reportList.getTotalElements();
            return new PageImpl<ReportDto>(
                    reportList.stream().map(report -> new ReportDto(
                                    report.getId(),
                                    report.getStudySet().getId(),
                                    report.getStudySet().getTitle(),
                                    accountRepository.findUserNameByUserId(report.getReporter().getId()),
                                    report.getContent(),
                                    userRepository.findById(report.getReporter().getId()).get().getAvatar(),
                                    String.valueOf(report.getCreatedTime()),
                                    report.isChecked()
                            )
                    ).collect(Collectors.toList()), pageable, totalElements);
        }
    }


    //get all the report's content containing
    public Page<ReportDto> getReportByContent(String content,Pageable pageable){
        Page<Report> reports = reportRepository.findByContentContains(content,pageable); //sort
        int totalElements = (int) reports.getTotalElements();
        return new PageImpl<ReportDto>(
                reports.stream().map(report -> new ReportDto(
                                report.getId(),
                                report.getStudySet().getId(),
                                report.getStudySet().getTitle(),
                                accountRepository.findUserNameByUserId(report.getReporter().getId()),
                                report.getContent(),
                                userRepository.findById(report.getReporter().getId()).get().getAvatar(),
                                String.valueOf(report.getCreatedTime()),
                                report.isChecked()
                        )
                ).collect(Collectors.toList()), pageable, totalElements);
    }


    public boolean checkReprotExistence(Long ssID) {
        Optional<Report> report = reportRepository.findByStudySetIdAndAndReporterId(ssID,
                authService.getCurrentUser().getId());
        return report.isPresent();
    }



    @Transactional
    public String saveReport(Long ssID,ObjectNode json){
        Report report = new Report();
        StudySet ss = studySetRepository.findById(ssID).orElseThrow(()
                ->  new ExpressionException("Lỗi ! ko tìm thấy Study set."));
        report.setStudySet(ss);
        report.setReporter(authService.getCurrentUser());
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
            json.put("SSOwner",accountRepository.findUserNameByUserId(ss.getCreator().getId()));
            json.put("numberOfReport",reportRepository.numberOfReportSS(ss.getId()));
            objectNodeList.add(json);
        }
        Collections.sort(objectNodeList, new Comparator<ObjectNode>() {
            @Override
            public int compare(ObjectNode o1, ObjectNode o2) {
                return o2.get("numberOfReport").asText().compareTo(o1.get("numberOfReport").asText());
            }
        });
        return objectNodeList;
    }

    @Transactional
    public List<ObjectNode> getAllReportOfSS(Long ssId){
        List<Report> reportList = reportRepository.findByStudySetId(ssId);
        List<ObjectNode> objectNodeList = new ArrayList<>();

        if(reportList.isEmpty()){
            return objectNodeList;
        }
        // helper create objectnode
        ObjectMapper mapper;
        for (Report r : reportList){
            mapper = new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("reportId",r.getId());
            json.put("content",r.getContent());
            json.put("reporter",accountRepository.findUserNameByUserId(r.getReporter().getId()));
            json.put("reportedDate",String.valueOf(r.getCreatedTime()));
            objectNodeList.add(json);
        }
        Collections.sort(objectNodeList, new Comparator<ObjectNode>() {
            @Override
            public int compare(ObjectNode o1, ObjectNode o2) {
                return o2.get("reportedDate").asText().compareTo(o1.get("reportedDate").asText());
            }
        });
        return objectNodeList;
    }

    @Transactional
    public void checkedReport(long[] reportsId){
        for (Long rpId : reportsId) {
            Report report = reportRepository.findById(rpId).orElseThrow(()
                    -> new ExpressionException("Lỗi ko tìm thấy Report"));
            report.setChecked(true);
            reportRepository.save(report);
        }
    }



}
