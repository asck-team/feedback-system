package org.asck.api.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(sequenceName = "SEQ_QUESTION", name = "SEQ_QUESTION_GEN", allocationSize = 1)
public class QuestionTableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_QUESTION_GEN")
	private Long id;

	@Column(name = "event_id")
	private Long eventId;

	@Column(name = "question_type_id")
	private Long questionTypeId;

	@Column(name = "question")
	@NotNull
	private String questionTitle;
	
	@Column(name = "order_nr")
	@NotNull
	private int order;
	
	@Column(name = "answer_required")
	private boolean answerRequired;

}
