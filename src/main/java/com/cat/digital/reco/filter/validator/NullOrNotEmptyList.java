/*
 *  Copyright (c) 2020 Caterpillar Inc. All Rights Reserved.
 *
 *  This work contains Caterpillar Inc.'s unpublished
 *  proprietary information which may constitute a trade secret
 *  and/or be confidential. This work may be used only for the
 *  purposes for which it was provided, and may not be copied
 *  or disclosed to others. Copyright notice is precautionary
 *  only, and does not imply publication.
 */

package com.cat.digital.reco.filter.validator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Annotation for a list to verify that it is either null, or not empty if it is not null.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotEmptyListValidator.class)
public @interface NullOrNotEmptyList {
  /**
   * Default error message.
   */
  String message() default "List must be null or not empty";

  /**
   * Groups property for annotation.
   */
  Class<?>[] groups() default {};

  /**
   * Payload property for annotation.
   */
  Class<? extends Payload>[] payload() default {};
}
