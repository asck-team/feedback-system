package org.asck.controller;

import java.util.List;

import org.asck.exceptions.EntityNotFoundException;
import org.asck.service.IFeedbackService;
import org.asck.service.model.Option;
import org.asck.service.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.Getter;

@RestController
@RequestMapping("/v1/feedback/events/{eventId}/questions/{questionId}/options")
@Getter(value = AccessLevel.PROTECTED)
public class QuestionOptionController {

	@Autowired
	private IFeedbackService feedbackService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Option>> readOptions(@PathVariable Long eventId, @PathVariable Long questionId)
			throws EntityNotFoundException {
		Question question = getFeedbackService().findQuestion(eventId, questionId);
		if (question.getOptions().isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(question.getOptions());
		}
	}

}
