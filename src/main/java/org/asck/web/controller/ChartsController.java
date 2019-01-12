package org.asck.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class ChartsController extends AbstractController {
	
	private static final Logger LOGGER = LogManager.getLogger(NewEventController.class);

	@GetMapping("/chart")
	public String createChart(@RequestParam("eventId") Long eventId, Model model) {
		
		List<Integer> data1 = Arrays.asList(1,3,7,3,5);
		List<Integer> data2 = Arrays.asList(4,8,2,5,7);
		List<Integer> data3 = Arrays.asList(5,4,3,6,8);
		List<Integer> data4 = Arrays.asList(3,8,2,5,7);
		List<Integer> data5 = Arrays.asList(2,6,2,5,7);
		List<AnswerReport> allAnswersToEventId = getFeedbackService().getAllAnswersToEventId(eventId);
		
		
		
		model.addAttribute("labels", getLabels(allAnswersToEventId));
		model.addAttribute("data1", data1);
		model.addAttribute("data2", data2);
		model.addAttribute("data3", data3);
		model.addAttribute("data4", data4);
		model.addAttribute("data5", data5);
		model.addAttribute("dataSet1", "Rating 1");
		model.addAttribute("dataSet2", "Rating 2");
		model.addAttribute("dataSet3", "Rating 3");
		model.addAttribute("dataSet4", "Rating 4");
		model.addAttribute("dataSet5", "Rating 5");
		return "chart";
	}

	private List<String> getLabels(List<AnswerReport> allAnswersToEventId) {
		List<String> labels = new ArrayList<String>();
		int i=0;
		for (AnswerReport answerReport : allAnswersToEventId) {
			if (answerReport.getQuestion().getQuestionType().contains("FIVE_SMILEYS")) {
			i=i+1;	
//			 labels.add(answerReport.getQuestion().getQuestionName());
			 labels.add("F" + i);
			LOGGER.info("Label {}", answerReport.getQuestion().getQuestionName());
			LOGGER.info("Label {}", answerReport.getQuestion().getQuestionType());
			}
		}
		
		return labels;
	}

}