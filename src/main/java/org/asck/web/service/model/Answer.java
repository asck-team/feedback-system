package org.asck.web.service.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Answer {

	@JsonCreator
	public static Answer createFromJson(@JsonProperty("questionId") Long questionId,
			@JsonProperty("optionId") Long optionId, @JsonProperty(required = false, value = "remark") String remark,
			@JsonProperty("answeredAt") @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ") LocalDateTime answeredAt) {
		return Answer.builder().optionId(optionId).questionId(questionId).remark(remark).answeredAt(answeredAt).build();
	}

	@JsonProperty("questionId")
	private Long questionId;
	@JsonProperty("optionId")
	private Long optionId;

	@JsonProperty("remark")
	private String remark;

	@JsonProperty("answeredAt")
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime answeredAt;

}
