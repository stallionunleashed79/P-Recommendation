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

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * This annotation ensures that custom validation for various Filter types is performed.
 */
@Constraint(validatedBy = {FilterValidator.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidFilter {

  String validFilterDefaultErrorMessage
          = "Filter validation failed due to filter attributes not being consistent with the rules associated with the filter type.";

  /**
   * Default message for validation annotation.
   */
  String message() default validFilterDefaultErrorMessage;

  /**
   * Annotation aux method.
   */
  Class<?>[] groups() default {};

  /**
   * Annotation aux method.
   */
  Class<? extends Payload>[] payload() default {};
}
