package org.asck.service.client.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerReport {
	
	private Question question;
	private Option option;
	private String remark;
	private LocalDateTime answeredAt;
	

}
