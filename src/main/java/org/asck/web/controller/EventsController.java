package org.asck.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
		org.asck.web.service.model.User userByEmail = getFeedbackService().getUserByEmail(getLoggedUser());
		model.addAttribute(MODEL_ATTR_EVENTS, getFeedbackService().leseAlleEvents(userByEmail.getId()));
		return UI_TEMPLATE_EVENTS;
	}
	
	@GetMapping("/deleteEvent")
	public String delete(@RequestParam("eventId") Long eventId, Model model) {
		getFeedbackService().deleteEvent(eventId);
		org.asck.web.service.model.User userByEmail = getFeedbackService().getUserByEmail(getLoggedUser());
		model.addAttribute(MODEL_ATTR_EVENTS, getFeedbackService().leseAlleEvents(userByEmail.getId()));
		return UI_TEMPLATE_EVENTS;
	}

	private String getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object pricipal = auth.getPrincipal();
		String loggedUser="";
		if (pricipal instanceof User) {
			loggedUser = ((User) pricipal).getUsername();
		}
		return loggedUser;
	}

}