package org.asck.api.repository;

import java.util.Optional;

import org.asck.api.repository.model.UserTableModel;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserTableModel, Long> {

	Optional<UserTableModel> findByEmail();
	
	
}
