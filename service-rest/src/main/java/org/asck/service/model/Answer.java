package org.asck.service.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter(AccessLevel.PUBLIC)
public class Answer {

	@JsonCreator
	public static Answer createFromJson(@JsonProperty("questionId") Long questionId, @JsonProperty("optionId") Long optionId, @JsonProperty(required = false, value = "remark") String remark,@JsonProperty("answeredAt") @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime answeredAt) {
		return Answer.builder().optionId(optionId).questionId(questionId).answeredAt(answeredAt).build();
	}
	
	@JsonProperty("questionId")
	@NotNull
	private Long questionId;
	@NotNull
	@JsonProperty("optionId")
	private Long optionId;

	@JsonProperty("remark")
	private String remark;

	@JsonProperty("answeredAt")
	@NotNull
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime answeredAt;

}
