package org.asck.web.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.web.service.model.Answer;
import org.asck.web.service.model.Event;
import org.asck.web.service.model.Option;
import org.asck.web.service.model.Question;
import org.asck.web.ui.model.AnswerForm;
import org.asck.web.ui.model.UiQuestionTM;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnswerController extends AbstractController {
	
	private static final String MODEL_ATTR_EVENTID = "eventId";
	private static final String MODEL_ATTR_QUESTIONS = "questions";
	private static final String MODEL_ATTR_ANSWER_FORM = "answerForm";
	private static final Logger LOGGER = LogManager.getLogger(AnswerController.class);

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
			List<Option> options = getFeedbackService().leseAlleOptionenZuEinerFrage(eventId, question.getId());
			LOGGER.info("{}",options);
			questionsUi.add(new UiQuestionTM(question.getId(), question.getQuestionName(), eventId, question.getQuestionType(), 0L, null, options, 0));
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
				q.setAnswerId(11L);
			}
			
			Answer answerToSave = new Answer(q.getId(), q.getAnswerId(), q.getAnswerFreeText(), LocalDateTime.now());
			LOGGER.info("{}", answerToSave);
			getFeedbackService().saveAnswer(answerToSave);
			
		}
		LOGGER.info("AnswerForm submitted!");
		return "thanks";
	}
}