package com.g18.controller;

import com.g18.dto.CardDto;
import com.g18.dto.CardLearningDto;
import com.g18.service.CardService;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/card")
@AllArgsConstructor
public class CardController {

	@Autowired
	private CardService cardService;

	@PostMapping("/create")
	public String createCard(@RequestBody List<CardDto> request) {
		return cardService.createCard(request);
	}

	@PutMapping("/edit")
	public String editCard(@RequestBody List<CardDto> request) {
		return cardService.editCard(request);
	}

	@DeleteMapping("/delete")
	public String deleteCard(@RequestParam(value = "id") Long id) {
		return cardService.deleteCard(id);
	}

	@PostMapping("/writeHint")
	public String writeHint(@RequestBody CardDto cardDto) {
		return cardService.writeHint(cardDto);
	}

	@GetMapping("/list")
	public ResponseEntity listStudySet(@RequestParam(value = "id") Long id) {
		return cardService.listCardByStudySet(id);
	}

	@PutMapping("/color")
	public String setColor(@RequestBody CardLearningDto request){
		return cardService.setColorCardLearning(request);
	}
}

