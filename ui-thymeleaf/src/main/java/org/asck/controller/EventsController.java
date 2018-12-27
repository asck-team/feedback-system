package org.asck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventsController extends AbstractController {

	private static final String MODEL_ATTR_EVENTS = "events";
	private static final String UI_TEMPLATE_EVENTS = "events";

	@GetMapping("/events")
	public String events(Model model) {
		model.addAttribute(MODEL_ATTR_EVENTS, getFeedbackService().leseAlleEvents());
		return UI_TEMPLATE_EVENTS;
	}
	
	@GetMapping("/deleteEvent")
	public String delete(@RequestParam("eventId") Long eventId, Model model) {
		getFeedbackService().deleteEvent(eventId);
		model.addAttribute(MODEL_ATTR_EVENTS, getFeedbackService().leseAlleEvents());
		return UI_TEMPLATE_EVENTS;
	}

}