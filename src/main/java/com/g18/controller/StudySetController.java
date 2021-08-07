package com.g18.controller;

import com.g18.dto.*;
import com.g18.entity.StudySet;
import com.g18.service.ReportService;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.service.StudySetService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("api/studySet")
@AllArgsConstructor
public class StudySetController {
    
	@Autowired
    private StudySetService studySetService;
	@Autowired
	private ReportService reportService;
	
	@GetMapping("/list")
    public ResponseEntity listStudySet(){
        return studySetService.listStudySet();
    }

	@GetMapping("/view")
    public ResponseEntity viewStudySet(@RequestParam(value="id") Long id){
        return studySetService.viewStudySetBy(id);
    }
	
	@PostMapping("/create")
	public ResponseEntity createStudySet(@Valid @RequestBody StudySetRequest request){
		return studySetService.createStudySet(request);
	}
	
	@PutMapping("/edit")
	public ResponseEntity editStudySet(@Valid @RequestBody StudySetRequest request){
		return studySetService.editStudySet(request);
	}

	@DeleteMapping("/delete")
	public ResponseEntity deleteStudySet(@RequestParam(value="id") Long id) {
		return studySetService.deleteStudySet(id);
	}

	@GetMapping("/export")
	public void exportToExcel(HttpServletResponse response, @RequestParam(value="id") Long id) throws IOException {
		studySetService.exportStudySetToExcel(response, id);
	}

	@GetMapping("/progress")
	public ResponseEntity viewStudySetProgress(@RequestParam(value="userId") Long userId, @RequestParam(value="studySetId") Long studySetId){
		return studySetService.getStudySetLearning(userId,studySetId);
	}

	@GetMapping("/listProgressByStudySet")
	public ResponseEntity getListProgressByStudySet(@RequestParam(value="id") Long request){
		return studySetService.getListProgressByStudySet(request);
	}

	@PutMapping("/color")
	public ResponseEntity setColor(@RequestParam(value="id") Long studySetId, @RequestParam(value="color") String color){
		return studySetService.setColorStudySetLearning(studySetId, color);
	}

	//create report
	@PostMapping(value = "/report/{ssId}")
	public String createReport(@PathVariable("ssId") Long ssId, @RequestBody ObjectNode json){
		return reportService.saveReport(ssId,json);
	}

	//check report
	@GetMapping("/checkReprotExistence/{ssId}")
	public boolean checkReprotExistence(@PathVariable("ssId") Long ssId) {
		return reportService.checkReprotExistence(ssId);

	}
}

