package com.g18.controller;

import com.g18.dto.CardDto;
import com.g18.dto.StudySetRequest;
import com.g18.dto.StudySetResponse;
import com.g18.entity.StudySet;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.service.StudySetService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/studySet")
@AllArgsConstructor
public class StudySetController {
    
	@Autowired
    private StudySetService studySetService;
	
	@GetMapping("/list")
    public ResponseEntity listStudySet(){
        return studySetService.listStudySet();
    }

	@GetMapping("/view")
    public ResponseEntity viewStudySet(@RequestParam(value="id") Long id){
        return studySetService.viewStudySetBy(id);
    }
	
	@PostMapping("/create")
	public String createRoom(@RequestBody StudySetRequest request){
		return studySetService.createStudySet(request);
	}
	
	@PutMapping("/edit")
	public String editStudySet(@RequestBody StudySetRequest request){
		return studySetService.editStudySet(request);
	}

	@DeleteMapping("/delete")
	public String deleteStudySet(@RequestParam(value="id") Long id) {
		return studySetService.deleteStudySet(id);
	}

	@GetMapping("/export")
		public void exportToExcel(HttpServletResponse response, @RequestParam(value="id") Long id) throws IOException {
		studySetService.exportStudySetToExcel(response, id);
	}
}

