package org.asck.web.controller;

import org.asck.web.view.ExcelView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExportAnswerDataController extends AbstractController{
	
	private static final String MODEL_ATTR_EVENTID = "eventId";
	
	@GetMapping(value = "/download")
	public ExcelView download(Model model, @RequestParam(required = true, name = MODEL_ATTR_EVENTID) Long eventId) {
		model.addAttribute("answers", getFeedbackService().getAllAnswersToEventId(eventId));
		return new ExcelView();
	}

}