package org.asck.web.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.asck.web.auth.email.validator.PasswordMatches;
import org.asck.web.auth.email.validator.PasswordValid;
import org.asck.web.auth.email.validator.ValidEmail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@PasswordMatches
public class User {
	
	@JsonCreator
	public static User createFromJson(@JsonProperty("id") Long id,
			@JsonProperty("email") String email, 
			@JsonProperty("password") String password) {
		return User.builder().id(id).email(email).password(password).build();
	}
	
	private Long id;
	@JsonProperty(required = true)
	@NotNull
    @NotEmpty
    @ValidEmail
	private String email;
	@JsonProperty(required = true)
	@NotNull
    @NotEmpty
    @PasswordValid
	private String password;
	@NotNull
    @NotEmpty
	private String passwordConfirm;
	public static final String ROLE = "USER";

}
