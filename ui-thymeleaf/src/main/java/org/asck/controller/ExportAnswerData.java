package org.asck.controller;

import org.asck.view.ExcelView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExportAnswerData extends AbstractController{
	
	private static final String MODEL_ATTR_EVENTID = "eventId";
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ExcelView download(Model model, @RequestParam(required = true, name = MODEL_ATTR_EVENTID) Long eventId) {
<<<<<<< HEAD
=======
		model.addAttribute("answers", getFeedbackService().getAllAnswersToEventId(eventId));
>>>>>>> branch 'feature/addRestControllerGetAlleAnswerToQuestion' of https://github.com/uniqueck/feedback-system.git
		return new ExcelView();
	}

}