package com.badenia.feedback.feedbacksystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.badenia.feedback.feedbacksystem.exceptions.EntityNotFoundException;
import com.badenia.feedback.feedbacksystem.service.IFeedbackService;
import com.badenia.feedback.feedbacksystem.service.model.Event;

import lombok.AccessLevel;
import lombok.Getter;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/feedback/events")
@Getter(AccessLevel.PROTECTED)
public class EventController {

	@Autowired
	private IFeedbackService feedbackService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Event>> getEvents() {
		List<Event> events = getFeedbackService().findEvents();
		if (events.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(events);
		}
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Event> getEvent(@PathVariable long id) throws EntityNotFoundException {
		return ResponseEntity.ok(getFeedbackService().findEventById(id));
	}

}
