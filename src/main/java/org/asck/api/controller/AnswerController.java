package org.asck.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.AccessLevel;
import lombok.Getter;

@RestController
@RequestMapping("/v1/feedback/answers")
@Getter(value = AccessLevel.PROTECTED)
public class AnswerController {

	@Autowired
	private IFeedbackService feedbackService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> saveAnswer(@Valid @RequestBody Answer model) throws EntityNotFoundException {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(getFeedbackService().saveAnswer(model)).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Answer>> getAllAnswersToQuestion(@RequestParam(name =  "questionId", required = true) Long questionId) throws EntityNotFoundException {
		List<Answer> allAnswersToQuestion = getFeedbackService().getAllAnswersToQuestion(questionId);
		if (allAnswersToQuestion.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(allAnswersToQuestion);
		}
	}

}
