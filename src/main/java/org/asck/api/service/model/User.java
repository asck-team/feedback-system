package org.asck.api.service.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
	
	@JsonCreator
	public static User create(@JsonProperty(required = false, value = "id") Long id, @JsonProperty("email") String email, 
			@JsonProperty("password") String password) {
		return builder().id(id).email(email).password(password).build();
	}
	
	private Long id;
	@NotNull
	private String email;
	@NotNull
	private String password;

}
