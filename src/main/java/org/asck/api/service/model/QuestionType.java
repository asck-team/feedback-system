package org.asck.api.service.model;

import lombok.Getter;

@Getter
public enum QuestionType {

	FREETEXT(1), THREE_SMILEYS(2), FIVE_SMILEYS(3), YES_NO(4);
	
	private long dbId;

	private QuestionType(long dbId) {
		this.dbId = dbId;
	}
	
	public static QuestionType getForDBId(long dbId) {
		for (QuestionType eachQuestionType : values()) {
			if (eachQuestionType.dbId == dbId) {
				return eachQuestionType;
			}
		}
		throw new IllegalArgumentException("Kein Mapping für Id '" + dbId + "'"); 
	}
	
	
}
