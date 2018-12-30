package org.asck.repository;

import java.util.List;
import java.util.Optional;

import org.asck.repository.model.QuestionTableModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionTableModel, Long> {

	List<QuestionTableModel> findAllByEventIdOrderByOrder(Long eventId);
	
	Optional<QuestionTableModel> findByEventIdAndId(Long eventId, Long id);
	
}
