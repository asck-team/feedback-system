package org.asck.api.repository;

import org.asck.api.repository.model.QuestionTypeTableModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTypeRepository extends JpaRepository<QuestionTypeTableModel, Long> {

}
