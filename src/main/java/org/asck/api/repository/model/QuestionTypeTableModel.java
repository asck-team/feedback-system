package org.asck.api.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Question_Type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionTypeTableModel {

	@Id
	private Long id;
	
	@Column
	private String description;
	
}
