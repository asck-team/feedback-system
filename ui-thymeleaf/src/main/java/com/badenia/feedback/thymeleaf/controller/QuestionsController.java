package com.badenia.feedback.thymeleaf.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jni.Time;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.badenia.feedback.thymeleaf.ui.model.AnswerForm;
import com.badenia.feedback.thymeleaf.ui.model.UiQuestionTM;
import com.feedback.service.client.model.Answer;
import com.feedback.service.client.model.Event;
import com.feedback.service.client.model.Question;

@Controller
public class QuestionsController extends AbstractController {
	
	private static final String MODEL_ATTR_EVENTID = "eventId";
	private static final String MODEL_ATTR_QUESTIONS = "questions";
	private static final String UI_TEMPLATE_QUESTIONS = "questions";
	private static final String MODEL_ATTR_ANSWER_FORM = "answerForm";
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
		
		AnswerForm answerForm = new AnswerForm();
		answerForm.setEventId(eventId);
		List<UiQuestionTM> questionsUi = new ArrayList<>();
		for (Question question : allQuestionsToEvent) {
			questionsUi.add(new UiQuestionTM(question.getId(), question.getQuestionName(), eventId, question.getQuestionType(), null, null));
		}
		answerForm.setQuestions(questionsUi);
		model.addAttribute(MODEL_ATTR_ANSWER_FORM, answerForm);
		return "answer";
	}
	
	@PostMapping(path = {"/answer"})
	public String answerPost(@ModelAttribute(value=MODEL_ATTR_ANSWER_FORM) AnswerForm answer) {
		LOGGER.info("Answer obj: {}", answer.toString());
		for (UiQuestionTM q : answer.getQuestions()) {
			LOGGER.info("{}", q.getId());
			LOGGER.info("{}", q.getEventId());
			LOGGER.info("{}", q.getQuestionName());
			LOGGER.info("{}", q.getAnswerType());
			LOGGER.info("{}", q.getAnswerId());
			LOGGER.info("{}", q.getAnswerFreeText());
			
			//FIXME AS 18.12.2018 Id auf UI setzen ... geht nicht. Muss weiter untersucht werden
			if (q.getAnswerFreeText()!=null) {
				q.setAnswerId(6L);
			}
			
			Answer answerToSave = new Answer(q.getId(), q.getAnswerId(), q.getAnswerFreeText(), LocalDateTime.now());
			System.out.println(answerToSave.toString());
			getFeedbackService().saveAnswer(answerToSave);
			
		}
		LOGGER.info("AnswerForm submitted!");
		return "redirect:/";
	}
	
	@GetMapping("/deleteQuestion")
	public String deleteQuestion(@RequestParam(MODEL_ATTR_EVENTID) Long eventId, @RequestParam("questionId") Long questionId, Model model) {
		getFeedbackService().deleteQuestion(eventId, questionId);
		return index(model, eventId);
	}
	
}
