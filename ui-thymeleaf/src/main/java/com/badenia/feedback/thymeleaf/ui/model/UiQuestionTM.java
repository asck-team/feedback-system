package com.badenia.feedback.thymeleaf.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UiQuestionTM {

	private Long id;

	private String questionName;

	private Long eventId;
	
	private String answerType;
	
	private Long answerId;
	
	private String answerFreeText;
	
	
}
