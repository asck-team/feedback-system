package org.asck.api.controller;

import java.net.URI;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.AccessLevel;
import lombok.Getter;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/feedback/user")
@Getter(AccessLevel.PROTECTED)
public class UserRestController {
	
private static final Logger LOGGER = LogManager.getLogger(EventRestController.class);
	
	@Autowired
	private IFeedbackService feedbackService;
	
	@GetMapping(path = "/{email:.+}/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) throws EntityNotFoundException {
		LOGGER.traceEntry();
		return ResponseEntity.ok(getFeedbackService().getUserByEmail(email));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(getFeedbackService().saveUser(User.builder().id(-1L).email(user.getEmail()).
				password(user.getPassword()).		
				build())).toUri();
		return ResponseEntity.created(uri).build();

	}
}
