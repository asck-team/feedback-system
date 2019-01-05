package org.asck.api.repository;

import java.util.List;

import org.asck.api.repository.model.QuestionOptionTableModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOptionTableModel, Long> {

	
	List<QuestionOptionTableModel> findAllByQuestionTypeId(Long questionTypeId);
	
	
	
	
}
