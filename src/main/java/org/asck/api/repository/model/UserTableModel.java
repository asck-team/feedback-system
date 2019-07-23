package org.asck.api.repository.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "SEQ_USER_GEN", sequenceName = "SEQ_USER")
@Table(name="user")
public class UserTableModel {
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_GEN")
	private Long id;
	
	@Column(name = "email", length = 40, nullable = false, unique = true)
	private String email;
	
	@Column(name = "password", length = 100, nullable = false)
	private String password;
	
	
	
	

}
