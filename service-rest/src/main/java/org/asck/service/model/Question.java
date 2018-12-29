package org.asck.service.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Question {

	@JsonCreator
	public static Question create(@JsonProperty(required = false, value = "id") Long id,
			@NotNull @JsonProperty("questionName") String questionName,
			@JsonProperty("questionType") String questionType) {
		return builder().id(id).questionName(questionName).questionType(QuestionType.valueOf(questionType)).build();
	}

	private Long id;

	@NotNull
	private String questionName;

	@NotNull
	@JsonFormat(shape = Shape.STRING)
	private QuestionType questionType;

	@JsonIgnore
	private List<Option> options;
	
	private int order;
	
	@JsonIgnore
	public boolean isIdSpecified() {
		return id != null && id != -1L;
	}
	
	@JsonIgnore
	public boolean isOrderSpecified() {
		return order > 0;
	}

}
