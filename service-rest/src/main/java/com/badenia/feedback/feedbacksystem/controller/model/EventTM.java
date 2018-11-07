package com.badenia.feedback.feedbacksystem.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class EventTM {

	private Long id;
	
	@NonNull
	private String title;

}
