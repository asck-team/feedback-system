package org.asck.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.exceptions.EntityNotFoundException;
import org.asck.service.IFeedbackService;
import org.asck.service.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*")
@RequestMapping("/v1/feedback/events")
@Getter(AccessLevel.PROTECTED)
public class EventController {

	private static final Logger LOGGER = LogManager.getLogger(EventController.class);
	
	@Autowired
	private IFeedbackService feedbackService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<Event>> getEvents() {
		LOGGER.traceEntry();
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

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createEvent(@Valid @RequestBody Event event) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(getFeedbackService().saveEvent(Event.builder().id(-1L).name(event.getName()).build())).toUri();
		return ResponseEntity.created(uri).build();

	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> updateEvent(@PathVariable("id") Long id, @Valid @RequestBody Event event) {
		getFeedbackService().saveEvent(Event.builder().id(id).name(event.getName()).build());
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Object> deleteEvent(@PathVariable("id") Long id) throws EntityNotFoundException {
		getFeedbackService().deleteEvent(id);
		return ResponseEntity.noContent().build();
	}

}
