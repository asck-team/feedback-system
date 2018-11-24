package com.badenia.feedback.thymeleaf.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.badenia.feedback.thymeleaf.model.Event;
import com.badenia.feedback.thymeleaf.model.Question;
import com.badenia.feedback.thymeleaf.service.FeedbackClientService;
import com.badenia.feedback.thymeleaf.service.IFeedbackClientService;

@Controller
public class EventsController {
	
	IFeedbackClientService serviceReposiroty = new FeedbackClientService();
	
	@GetMapping("/events")
	public String index(Model model) {
		List<Event> allEvents = serviceReposiroty.leseAlleEvents();
		model.addAttribute("events", allEvents);
		return "events";
	}
}