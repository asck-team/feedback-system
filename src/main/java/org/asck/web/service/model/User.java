package org.asck.web.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
	
	private long id;
	private String name;
	private String email;
	private String password;
	private String role;
}
