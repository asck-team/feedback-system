package org.asck.repository;

import org.asck.repository.model.QuestionTypeTableModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionTypeRepository extends JpaRepository<QuestionTypeTableModel, Long> {

}
