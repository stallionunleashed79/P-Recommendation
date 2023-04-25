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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Validator for a list to verify that it is either null, or not empty if it is not null.
 */
public class NullOrNotEmptyListValidator implements ConstraintValidator<NullOrNotEmptyList, List<?>> {

  /**
   * Checks if list is valid or not.
   *
   * @param list                       list of objects to validate
   * @param constraintValidatorContext to give info about validator
   * @return whether list is valid or not. Valid values are null, or a non-empty list
   */
  public boolean isValid(List<?> list, ConstraintValidatorContext constraintValidatorContext) {
    return list == null || !list.isEmpty();
  }
}