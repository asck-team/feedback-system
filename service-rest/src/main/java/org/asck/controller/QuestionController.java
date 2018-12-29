package org.asck.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.asck.exceptions.EntityNotFoundException;
import org.asck.service.IFeedbackService;
import org.asck.service.model.Event;
import org.asck.service.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.AccessLevel;
import lombok.Getter;

@RestController
@RequestMapping("/v1/feedback/events/{eventId}/questions")
@Getter(value = AccessLevel.PROTECTED)
public class QuestionController {

	@Autowired
	private IFeedbackService feedbackService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Question>> readQuestions(@PathVariable("eventId") Long eventId)
			throws EntityNotFoundException {
		Event event = getFeedbackService().findEventById(eventId);
		if (event.getQuestions().isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(event.getQuestions());
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/{questionId}")
	public ResponseEntity<Question> readQuestion(@PathVariable("eventId") Long eventId,
			@PathVariable("questionId") Long questionId) throws EntityNotFoundException {
		Question question = getFeedbackService().findQuestion(eventId, questionId);
		return ResponseEntity.ok(question);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createQuestion(@PathVariable("eventId") Long eventId,
			@Valid @RequestBody Question question) {
		Long questionId = getFeedbackService().save(eventId, Question.builder().id(-1L)
				.questionName(question.getQuestionName()).questionType(question.getQuestionType()).build());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(questionId).toUri();
		return ResponseEntity.created(uri).build();

	}

	@PutMapping(path = "/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> updateQuestion(@PathVariable("eventId") Long eventId,
			@PathVariable("questionId") Long questionId, @Valid @RequestBody Question question) {
		getFeedbackService().save(eventId, Question.builder().id(questionId).questionName(question.getQuestionName())
				.questionType(question.getQuestionType()).build());
		return ResponseEntity.noContent().build();

	}
	
	@DeleteMapping(path = "/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> deleteQuestion(@PathVariable("eventId") Long eventId,
			@PathVariable("questionId") Long questionId) throws EntityNotFoundException {
		getFeedbackService().deleteQuestion(eventId, questionId);
		return ResponseEntity.noContent().build();

	}

}
