package com.cat.digital.reco.validators.email;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConditionalEmailValidator.class})

@Documented
public @interface ConditionalEmail {

  String message() default "Wrong email";
  Class<?>[] groups() default { };
  Class<? extends Payload>[] payload() default { };

}