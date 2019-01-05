package org.asck.web.ui.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerForm {
	
	private long eventId;
	List<UiQuestionTM> questions = new ArrayList<UiQuestionTM>(); 

}
