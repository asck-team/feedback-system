package org.asck.web.ui.model;

import java.util.List;

import org.asck.web.service.model.Option;

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
	
	private List<Option> options;
	
	private int order;
	
	
}
