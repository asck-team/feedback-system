package org.asck.web.auth.email.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String>{
	
	@Override
    public void initialize(final PasswordValid constraintAnnotation) {
    }
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (validatePassword(value));
	}

	private boolean validatePassword(String value) {
		if (value.length() < 8) {
			return false;
		}
		return true;
	}


}
