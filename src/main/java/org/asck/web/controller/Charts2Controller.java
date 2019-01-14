package org.asck.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asck.web.service.model.AnswerReport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AccessLevel;
import lombok.Getter;

@Controller
@Getter(value = AccessLevel.PROTECTED)
public class Charts2Controller extends AbstractController {
	
	private static final Logger LOGGER = LogManager.getLogger(NewEventController.class);
	
	@GetMapping("/chart2")
	public String barGraph(@RequestParam("eventId") Long eventId, Model model) {
		List<AnswerReport> allAnswersToEventId = getFeedbackService().getAllAnswersToEventId(eventId);
		Map<String, List<Integer>> surveyMap = getSurveyMap(allAnswersToEventId);
		
		System.out.println(surveyMap.keySet());
		System.out.println(surveyMap.values());
		
		
		
		model.addAttribute("surveyMap", surveyMap);
		return "chart2";
	}

	private Map<String, List<Integer>> getSurveyMap(List<AnswerReport> allAnswersToEventId) {
		Map<String, List<Integer>> result = new LinkedHashMap<>();
		
		for (AnswerReport answerReport : allAnswersToEventId) {
			if (answerReport.getQuestion().getQuestionType().contains("FIVE_SMILEYS")) {
				String key = "Question" + answerReport.getQuestion().getId();
				if (!result.containsKey(key)) {
				result.put(key, getAllAnswersToQuestionId(answerReport.getQuestion().getId(), allAnswersToEventId));
				}
			}
		}
		
		return result;
	}

	private List<Integer> getAllAnswersToQuestionId(Long questionId, List<AnswerReport> allAnswersToEventId) {
		List<Integer> allAnswersToQuestionId = new ArrayList<>();
		int countRating1 = 0;
		int countRating2 = 0;
		int countRating3 = 0;
		int countRating4 = 0;
		int countRating5 = 0;
		for (AnswerReport answerReport : allAnswersToEventId) {
			if (answerReport.getQuestion().getId() == questionId) {
				if (answerReport.getOption().getId() == 8) {
					countRating5 = countRating5 + 1;
				}
				if (answerReport.getOption().getId() == 7) {
					countRating4 = countRating4 + 1;
				}
				if (answerReport.getOption().getId() == 6) {
					countRating3 = countRating3 + 1;
				}
				if (answerReport.getOption().getId() == 5) {
					countRating2 = countRating2 + 1;
				}
				if (answerReport.getOption().getId() == 4) {
					countRating1 = countRating1 + 1;
				}
			}
		}
		allAnswersToQuestionId.add(countRating1);
		allAnswersToQuestionId.add(countRating2);
		allAnswersToQuestionId.add(countRating3);
		allAnswersToQuestionId.add(countRating4);
		allAnswersToQuestionId.add(countRating5);
		return allAnswersToQuestionId;
	}

	@GetMapping("/displayPieChart")
	public String pieChart(Model model) {
		model.addAttribute("pass", 50);
		model.addAttribute("fail", 50);
		return "pieChart";
	}

}
