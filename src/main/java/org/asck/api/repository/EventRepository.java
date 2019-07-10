package org.asck.api.repository;

import org.asck.api.repository.model.EventTableModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventTableModel, Long> {

		public List<EventTableModel> findAllByOwnedBy(Long ownedBy);
	
}
