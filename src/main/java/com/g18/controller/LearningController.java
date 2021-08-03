package com.g18.controller;

import com.g18.dto.CardQualityRequestUpdate;
import com.g18.dto.LearnRequestDto;
import com.g18.service.LearningService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/learn")
@AllArgsConstructor
public class LearningController {

	@Autowired
	private LearningService learningService;

	@GetMapping("/studySet")
	public ResponseEntity learnByStudySet(@RequestParam(value="id") Long id){
		return learningService.learningFlashCardByStudySet(id);
	}

	@PutMapping("/update")
	public ResponseEntity updateAfterLearn(@Valid @RequestBody CardQualityRequestUpdate cardQualityRequestUpdate){
		return learningService.updateCardLearning(cardQualityRequestUpdate);
	}

	@GetMapping("/learnByDate")
	public ResponseEntity learnToday(@RequestParam(value="studySet") Long studySetId, @RequestParam(value="date") String date){
		return learningService.learningFlashCardByDateAndStudySetAndUser(studySetId, date);
	}

	@GetMapping("continue")
	public ResponseEntity learnContinue(@RequestParam(value="studySetId") Long studySetId ){
		return learningService.learningContinue(studySetId);
	}

	@GetMapping("/listCardSort")
	public ResponseEntity getListCardLearningOrderByLearnedDate(@RequestParam(value="id") Long request){
		return learningService.getListCardLearningOrderByLearnedDate(request);
	}
}

