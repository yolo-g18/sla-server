package com.g18.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.dto.DisableSetDto;
import com.g18.dto.ReportDto;
import com.g18.entity.StudySet;
import com.g18.repository.StudySetRepository;
import com.g18.service.CardService;
import com.g18.service.EmailSenderService;
import com.g18.service.ReportService;
import com.g18.service.StudySetService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AdminController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private StudySetRepository studySetRepository;
    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private StudySetService studySetService;

    @Autowired
    private CardService cardService;

    //get all report
    @GetMapping("/report/all")
    public Page<ReportDto> getAllReports(Pageable pageable) {
        return reportService.getAllReport(pageable);
    }

    //api get by true false

    //get all report
    @GetMapping("/report")
    public Page<ReportDto> getReportsContentContains(Pageable pageable,@RequestParam String content) {
        return reportService.getReportByContent(content,pageable);
    }
    //get all reports, filter by isPublic (true or false)
    @GetMapping("/report/filter")
    public Page<ReportDto> getReportFilterByIsPublic(Pageable pageable,@RequestParam boolean isChecked){
        return reportService.getReportFilterByIsChecked(pageable,isChecked);
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

    @GetMapping("/studySet/view")
    public ResponseEntity viewStudySet(@RequestParam(value="id") Long id){
        return studySetService.viewStudySetAdmin(id);
    }

    @GetMapping("card/list")
    public ResponseEntity listStudySet(@RequestParam(value = "id") Long id) {
        return cardService.listCardByStudySetAdmin(id);
    }

    @PutMapping("/report/disable")
    public String deleteReport(@RequestBody DisableSetDto payload) throws NotFoundException, MessagingException {
        StudySet ss = studySetRepository.findById(payload.getId()).orElseThrow(()
                ->  new ExpressionException("Lỗi ko tìm thấy")) ;
        System.out.println("Email: " + ss.getCreator().getEmail());
        if(payload.isActive() == false) {
            emailSenderService.sendSimpleEmail(
                    ss.getCreator().getEmail(),
                    "Hi " + ss.getCreator().getLastName() +" " + ss.getCreator().getFirstName()
                            + "\nYour study set : " +ss.getTitle()+ " violate our polices. The study set is disabled." +
                            "\nEmail us if you need any support !",
                    "[SLA] Your study set is disabled");
        } else {
            emailSenderService.sendSimpleEmail(
                    ss.getCreator().getEmail(),
                    "Hi " + ss.getCreator().getLastName() +" " + ss.getCreator().getFirstName()
                            + "\nYour study set : " +ss.getTitle()+ "is reinstated" +
                            "\nAfter careful consideration, we have decided to reinstate your study set." +
                            "\nEmail us if you need any support !",
                    "[SLA] Your study set is reinstated");
        }

        ss.setActive(payload.isActive());
        studySetRepository.save(ss);

        return "Successfully !";
    }

    @PutMapping("/report/check")
    public String checkedReports(@RequestBody long[] reportsId){
        reportService.checkedReport(reportsId);
        return "Checked !";
    }




}
