package com.g18.controller;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g18.service.StudySetService;

@RestController
@RequestMapping("api/studySet")
@AllArgsConstructor
public class StudySetController {
    
	@Autowired
    private StudySetService studySetService;
	
	@GetMapping("/list")
    public List<ObjectNode> listStudySet(@RequestBody ObjectNode json){
        return studySetService.listStudySet(json);
    }
	
	@GetMapping("/view/{id}")
    public ObjectNode  viewStudySet(@PathVariable Long id){
        return studySetService.viewStudySetBy(id);
    }
	
	@PostMapping("/create")
	public String createRoom(@RequestBody ObjectNode json){ 
		return studySetService.createStudySet(json);
	}
	
	@PutMapping("/edit")
	public String editStudySet(@RequestBody ObjectNode json){
		return studySetService.editStudySet(json);
	}
	
	@DeleteMapping("/delete")
	public String deleteStudySet(@RequestParam(value="id") Long id) {
		return studySetService.deleteStudySet(id);
	}
	
	
	
	

	    

}

