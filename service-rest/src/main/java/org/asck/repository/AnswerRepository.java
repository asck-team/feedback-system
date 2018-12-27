package org.asck.repository;

import java.util.List;

import org.asck.repository.model.AnswerTableModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<AnswerTableModel, Long> {

	List<AnswerTableModel> findAllByQuestionId(Long questionId);
	
}
