package org.asck.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.asck.api.service.model.QuestionType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.Getter;

@RestController
@RequestMapping("/v1/feedback/admin")
@Getter(value = AccessLevel.PROTECTED)
public class AdminRestController {

	@GetMapping(path = { "/questionTypes" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> readQuestionTypes() {
		return ResponseEntity
				.ok(Arrays.asList(QuestionType.values()).stream().map(QuestionType::name).collect(Collectors.toList()));
	}

}
