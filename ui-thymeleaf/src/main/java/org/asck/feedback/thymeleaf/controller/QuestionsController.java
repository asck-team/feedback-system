package org.asck.feedback.thymeleaf.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.feedback.thymeleaf.ui.model.AnswerForm;
import org.asck.service.client.model.Event;
import org.asck.service.client.model.Question;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionsController extends AbstractController {
	
	private static final String MODEL_ATTR_EVENTID = "eventId";
	private static final String MODEL_ATTR_QUESTIONS = "questions";
	private static final String UI_TEMPLATE_QUESTIONS = "questions";
	private static final Logger LOGGER = LogManager.getLogger(QuestionsController.class);

	@GetMapping(path = {"/questions"}, params = MODEL_ATTR_EVENTID)
	public String index(Model model, @RequestParam(required = true, name = MODEL_ATTR_EVENTID) Long eventId) {
		List<Question> allQuestionsToEvent = getFeedbackService().leseAlleFragenZuEvent(eventId);
		model.addAttribute(MODEL_ATTR_QUESTIONS, allQuestionsToEvent);
		model.addAttribute(MODEL_ATTR_EVENTID, eventId);
		return UI_TEMPLATE_QUESTIONS;
	}
	
	@GetMapping(path = {"/answer"}, params = {MODEL_ATTR_EVENTID})
	public String answer(Model model, @RequestParam(required = true, name = MODEL_ATTR_EVENTID) Long eventId) {
		Event eventById = getFeedbackService().getEventById(eventId);
		List<Question> allQuestionsToEvent = getFeedbackService().leseAlleFragenZuEvent(eventId);
		model.addAttribute(MODEL_ATTR_QUESTIONS, allQuestionsToEvent);
		model.addAttribute(MODEL_ATTR_EVENTID, eventId);
		model.addAttribute("eventName", eventById.getName());
		model.addAttribute("answerForm", new AnswerForm(eventId, ""));
		return "answer";
	}
	
	@PostMapping(path = {"/answer"})
	public String answerPost(@ModelAttribute(value="answerForm") AnswerForm answer) {
		LOGGER.info("Answer obj: {}", answer.toString());
		LOGGER.info("AnswerForm submitted!");
		return "redirect:/";
	}
	
	@GetMapping("/deleteQuestion")
	public String deleteQuestion(@RequestParam(MODEL_ATTR_EVENTID) Long eventId, @RequestParam("questionId") Long questionId, Model model) {
		getFeedbackService().deleteQuestion(eventId, questionId);
		return index(model, eventId);
	}
	
}
