package com.g18.controller;

import com.g18.service.LearningService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping("/update")
	public ResponseEntity updateAfterLearn(@RequestParam(value="cardId") Long cardId, @RequestParam(value="q") Integer q){
		return learningService.updateCardLearning(cardId,q);
	}

	@GetMapping("/today")
	public ResponseEntity learnToday(){
		return learningService.learningFlashCardToday();
	}

	@GetMapping("continue")
	public ResponseEntity learnContinue(@RequestParam(value="studySetId") Long studySetId ){
		return learningService.learningContinue(studySetId);
	}
}

