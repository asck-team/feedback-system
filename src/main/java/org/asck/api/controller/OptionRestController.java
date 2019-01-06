package org.asck.api.controller;

import org.asck.api.exceptions.EntityNotFoundException;
import org.asck.api.service.IFeedbackService;
import org.asck.api.service.model.Option;
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
@RequestMapping("/v1/feedback/options")
@Getter(value = AccessLevel.PROTECTED)
public class OptionRestController {
	
	@Autowired
	private IFeedbackService feedbackService;

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Option> findOptionById(@PathVariable long id)
			throws EntityNotFoundException {
		return ResponseEntity.ok(getFeedbackService().getOptionById(id));
	}

}
