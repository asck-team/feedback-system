package org.asck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExportAnswerData extends AbstractController{
	
	@GetMapping(value = "/download")
	public String download(Model model) {
	    Long eventId = 1L;
		model.addAttribute("answers", getFeedbackService().getAllAnswersToEventId(eventId));
	    return "";
	}

}
