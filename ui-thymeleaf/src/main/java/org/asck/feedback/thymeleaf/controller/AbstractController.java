package org.asck.feedback.thymeleaf.controller;

import org.asck.service.client.IFeedbackClientService;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
abstract class AbstractController {

	@Autowired private IFeedbackClientService feedbackService;
	
	public AbstractController() {
	}

}
