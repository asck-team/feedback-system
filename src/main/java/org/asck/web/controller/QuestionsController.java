package org.asck.web.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.web.service.model.Question;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionsController extends AbstractController {
	
	private static final String MODEL_ATTR_EVENTID = "eventId";
	private static final String MODEL_ATTR_QUESTIONS = "questions";
	private static final String UI_TEMPLATE_QUESTIONS = "questions";
	
	private static final Logger LOGGER = LogManager.getLogger(QuestionsController.class);

	@GetMapping(path = {"/questions"}, params = MODEL_ATTR_EVENTID)
	public String index(Model model, @RequestParam(required = true, name = MODEL_ATTR_EVENTID) Long eventId) {
		LOGGER.traceEntry("with parameter {}", eventId);
		List<Question> allQuestionsToEvent = getFeedbackService().leseAlleFragenZuEvent(eventId);
		model.addAttribute(MODEL_ATTR_QUESTIONS, allQuestionsToEvent);
		model.addAttribute(MODEL_ATTR_EVENTID, eventId);
		return UI_TEMPLATE_QUESTIONS;
	}
	
	@GetMapping("/deleteQuestion")
	public String deleteQuestion(@RequestParam(MODEL_ATTR_EVENTID) Long eventId, @RequestParam("questionId") Long questionId, Model model) {
		getFeedbackService().deleteQuestion(eventId, questionId);
		return index(model, eventId);
	}
	
	@GetMapping("/updateOrderQuestion")
	public String updateOrderQuestion(@RequestParam(MODEL_ATTR_EVENTID) Long eventId, @RequestParam("questionId") Long questionId, @RequestParam("order") int order, @RequestParam("qType") String qType,@RequestParam("qName") String qName, Model model) {
		LOGGER.info("Order aktualisieren!!!!!!!!");
		LOGGER.info("Eventid: {}, QuestionID: {}, Order: {}, QuestionType: {}, QuestionName: {}", eventId, questionId, order, qType, qName);
		Question questionToSave = new Question(questionId, qName, qType, order);
		getFeedbackService().saveQuestion(eventId, questionToSave);
		return "redirect:/questions?" + MODEL_ATTR_EVENTID + "=" + eventId;
	}
	
}
