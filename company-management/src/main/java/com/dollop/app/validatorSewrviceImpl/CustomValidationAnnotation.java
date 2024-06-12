package com.dollop.app.validatorSewrviceImpl;

import java.util.Objects;

import com.dollop.app.validatorService.CustomValidator;

import ch.qos.logback.core.joran.conditional.IfAction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotEmpty;

public class CustomValidationAnnotation implements ConstraintValidator<CustomValidator, String> {

	private CustomValidator.Type type;

	@Override
	public void initialize(CustomValidator constraintAnnotation) {
		this.type = constraintAnnotation.type();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Boolean	result = Boolean.FALSE;
		if (value == null || value.isEmpty()) {
			setMessage(type.toString().toLowerCase()+" is required", context);
				return false;
		}
		System.err.println("TYPE :: "+type);
		switch (type) {
		case DESCRIPTION:
			if (Objects.nonNull(value) ) {
				setMessage("DESCRIPTION is required", context);
				
				result = Boolean.TRUE;				
				break;
			}
			setMessage("DESCRIPTION is required", context);
			result = Boolean.TRUE;
			break;
		
		case GENDER:
			if (Objects.nonNull(value)) {
				result = Boolean.TRUE;				
				break;
			}
			setMessage("GENDER is required", context);
			result = Boolean.TRUE;
			break;
		
		case EMAIL:
			if (isValidEmail(value) && Objects.nonNull(value)) {
				result = Boolean.TRUE;				
				break;
			}
			setMessage("Invalid Email Address", context);
			result = Boolean.TRUE;
			break;
		case PHONE:
			if (isValidPhoneNumber(value) && Objects.nonNull(value)) {
				result = Boolean.TRUE;
			break;
			}
			setMessage("Invalid Phone Number", context);
			result = Boolean.TRUE;
		case NAME:
			
			if (Objects.nonNull(value) || value.length()>2 && value.length()<25 || isValidName(value)) {
				result = Boolean.TRUE;
				setMessage("Name is required", context);
				
				break;
			}
			System.err.println(value.length());
			if(value.length()< 4 || value.length()>25) {
				System.err.println("second");
				setMessage("Name length is not valid", context);
				result = Boolean.TRUE;
				break;
			}
//			System.err.println("third");
			setMessage("Name is required ", context);
			result = Boolean.TRUE;
			break;
		case PASSWORD:
			if (Objects.nonNull(value) && isValidPassword(value) ){
				result = Boolean.TRUE;
				break;
			}
			setMessage("Password Invalid is required", context);
			result = Boolean.TRUE;
			break;
		case REQUIRED:
			if (Objects.nonNull(value)  ){
				result = Boolean.TRUE;
				setMessage("REQUIRED ARE  INVALID ", context);
				
				break;
			}
			setMessage("REQUIRED ARE  INVALID ", context);
			result = Boolean.TRUE;
			break;
		case BOTH:
			if (Objects.nonNull(value)  && isValidBoth(value) ){
				result = Boolean.TRUE;
				break;
			}
			setMessage("Both are  Invalid ", context);
			result = Boolean.TRUE;
			break;
		case SSN:
			if (Objects.nonNull(value)  && isValidBoth(value) ){
				result = Boolean.TRUE;
				break;
			}
			if(value.length()<=6 || value.length()>=6) {
				System.err.println("second");
				setMessage("ssn length is not valid", context);
				result = Boolean.TRUE;
				break;
			}
			setMessage("SSN Invalid is required", context);
			result = Boolean.TRUE;
			break;
			
		case ALL:
			
			
		default:
			// Default to invalid if an unsupported type is provided
			return false;
		}
		return result;
	}
	
	
	
	private boolean isValidPhoneSsn(String value) {
		// Basic phone number validation regex
		// This example allows digits, spaces, and optional parentheses and dashes
		String phoneRegex ="^[A-Z]{2}[A-Z0-9]{4}$";
		return value.matches(phoneRegex);
	}

	private boolean isValidPhoneNumber(String value) {
		String phoneRegex = "/^[6-9]{1}[0-9]{9}$/";
		return value.matches(phoneRegex);
	}

	private boolean isValidEmail(String value) {
		// Basic email validation regex
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-z]+\\.)+[a-zA-Z]{2,7}$";
		return value.matches(emailRegex);
	}

	private boolean isValidName(String value) {
		String emailRegex = "^[A-Z]{1}([a-z]*\s*)+$";
		return value.matches(emailRegex);
	}
	private boolean isValidPassword(String value) {
		// Basic email validation regex
		String emailRegex = "/^(?=.*[a-zA-Z])(?=.*\\d)/";
		return value.matches(emailRegex);
	}
	private boolean isValidBoth(String value) {
		// Basic email validation regex
		String emailRegex = "/^[A-Z]{1}([a-z]*\\s*)+$/";
		return value.matches(emailRegex);
	}
	private void setMessage(String message, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}

}
