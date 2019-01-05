package org.asck.api.repository;

import java.util.List;

import org.asck.api.repository.model.AnswerTableModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerTableModel, Long> {

	List<AnswerTableModel> findAllByQuestionId(Long questionId);
	
}
