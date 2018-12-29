package org.asck.repository.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Answer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "SEQ_ANSWER_GEN", sequenceName = "SEQ_ANSWER")
public class AnswerTableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ANSWER_GEN")
	private Long id;

	@Column(name = "answered_at")
	private Date answeredAt;

	@Column(name = "remark")
	private String remark;

	@Column(name = "question_option_id")
	private Long questionOptionId;

	@Column(name = "question_id")
	private Long questionId;

}
