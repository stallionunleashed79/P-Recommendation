package com.cat.digital.reco.validators.interfaces;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cat.digital.reco.validators.implementations.ExpirationDateValidator;

@Documented
@Constraint(validatedBy = { ExpirationDateValidator.class })
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IExpirationDateValidator {

  String message() default "Expiration date should be within a year from today.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
