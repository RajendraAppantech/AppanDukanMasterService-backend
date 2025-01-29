package com.appan;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegexValidator implements ConstraintValidator<ValidRegex, String> {

	@Override
	public void initialize(ValidRegex constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return true;
		}

		try {

			Pattern.compile(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
