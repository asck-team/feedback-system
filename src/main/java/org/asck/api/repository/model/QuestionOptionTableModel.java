package org.asck.api.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "question_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionTableModel {

	@Id
	private Long id;
	
	@Column(name = "question_type_id")
	private Long questionTypeId;
	
	@Column(name = "optionial_description")
	private String optionalDescription;
	
	@Column(name = "icon_path")
	private String iconPath;
	
}
