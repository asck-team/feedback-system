package org.asck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExportAnswerData extends AbstractController{
	
	private static final String MODEL_ATTR_EVENTID = "eventId";
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public String download(Model model) {
	    Long eventId = 1L;
		model.addAttribute("answers", getFeedbackService().getAllAnswersToEventId(eventId));
	    return "";
	}

}
