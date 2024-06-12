package com.dollop.app.validatorService;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dollop.app.validatorSewrviceImpl.CustomValidationAnnotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CustomValidationAnnotation.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
public @interface CustomValidator {

	String message() default "Invalid value";
	Class<?> [] groups() default {};
	Class<? extends Payload> []  payload() default {};
	
	 // Add a type attribute to specify the type of input (email or phone number)
    Type type() default Type.ALL;

    enum Type {
        EMAIL,
        PHONE,
        NAME,
        PASSWORD,
        ALL,
        BOTH,
        GENDER,  
        DESCRIPTION,
        SSN,
        
        REQUIRED,
        
    }}
