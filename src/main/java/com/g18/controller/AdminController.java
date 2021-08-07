package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.ReportDto;
import com.g18.entity.StudySet;
import com.g18.repository.StudySetRepository;
import com.g18.service.EmailSenderService;
import com.g18.service.ReportService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private StudySetRepository studySetRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    //create report
    @PostMapping(value = "/report/{ssId}")
    public String createReport(@PathVariable("ssId") Long ssId, @RequestBody ObjectNode json){
        return reportService.saveReport(ssId,json);
    }

    //get all report
    @GetMapping("/report/all")
    public Page<ReportDto> getAllReports(Pageable pageable) {
        return reportService.getAllReport(pageable);
    }

    //get all report
    @GetMapping("/report")
    public Page<ReportDto> getReportsContentContains(Pageable pageable,@RequestParam String content) {
        return reportService.getReportByContent(content,pageable);
    }
    //get all Study Set has be reported
    @GetMapping("/report/ss")
    public Page<ObjectNode> getSSHasReport(@SortDefault(sort = "numberOfReport", direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        List<ObjectNode> nodes = reportService.getSSReport();
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > nodes.size() ? nodes.size()
                : (start + pageable.getPageSize()));

        return new PageImpl<ObjectNode>(nodes.subList(start, end), pageable, nodes.size());
    }

    //Get all report of a StudySet
    @GetMapping("/report/{ssId}")
    public Page<ObjectNode> getAllReportOfSS(@PathVariable("ssId") Long ssId,Pageable pageable) throws IOException {
        List<ObjectNode> nodes = reportService.getAllReportOfSS(ssId);


        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > nodes.size() ? nodes.size()
                : (start + pageable.getPageSize()));

        return new PageImpl<ObjectNode>(nodes.subList(start, end), pageable, nodes.size());
    }


    @DeleteMapping("/report/delete/{ssId}")
    public String deleteReport(@PathVariable("ssId") Long ssId) throws NotFoundException, MessagingException {
        StudySet ss = studySetRepository.findById(ssId).orElseThrow(()
                ->  new ExpressionException("Lỗi ko tìm thấy")) ;
        System.out.println("Email: " + ss.getCreator().getEmail());
        emailSenderService.sendEmailWithAttachment(
                ss.getCreator().getEmail(),
                "Hi there your study set" +ss.getTitle()+ " has been report. We deleted it",
                "Delete study set be reported",
                "C:\\Users\\congv\\OneDrive\\Máy tính\\Marketing-Success-Image-1.jpg");
        studySetRepository.delete(ss);
        return "Delete study set successfully !";
    }

    @PutMapping("/report/check")
    public String checkedReports(@RequestBody long[] reportsId){
        reportService.checkedReport(reportsId);
        return "Checked !";
    }




}
