package org.asck.api.repository;

import java.util.List;
import java.util.Optional;

import org.asck.api.repository.model.QuestionTableModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionTableModel, Long> {

	List<QuestionTableModel> findAllByEventIdOrderByOrder(Long eventId);
	
	Optional<QuestionTableModel> findByEventIdAndId(Long eventId, Long id);
	
}
