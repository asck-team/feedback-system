package org.asck.repository;

import java.util.List;

import org.asck.repository.model.QuestionOptionTableModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOptionRepository extends JpaRepository<QuestionOptionTableModel, Long> {

	
	List<QuestionOptionTableModel> findAllByQuestionTypeId(Long questionTypeId);
	
	
	
	
}
