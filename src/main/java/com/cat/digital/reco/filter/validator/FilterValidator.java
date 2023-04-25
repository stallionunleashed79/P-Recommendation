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

import com.cat.digital.reco.filter.Filter;
import com.cat.digital.reco.filter.type.TimeRangeFilter;
import org.springframework.util.ObjectUtils;

/**
 * Performs validation of {@link Filter} object. For more info see {@link ValidFilter}.
 */
public class FilterValidator implements ConstraintValidator<ValidFilter, Filter> {

  /**
   * Checks validity of the given {@link Filter} object, by checking if the filter values conform to the filter key enums if applicable.
   *
   * @param filter                     {@linkplain Filter}, {@link ValidFilter}
   * @param constraintValidatorContext {@linkplain ConstraintValidatorContext}
   */
  @Override
  public boolean isValid(Filter filter, ConstraintValidatorContext constraintValidatorContext) {

    if (ObjectUtils.isEmpty(filter.getPropertyName())) {
      overrideDefaultConstraintViolation(constraintValidatorContext, "propertyName",
                                         Filter.propertyNameParameterIsNotPresent);
      return false;
    }

    switch (filter.getType()) {

      case timeRange:
        TimeRangeFilter timeRangeFilter = (TimeRangeFilter) filter;
        return validateTimeRangeFilter(timeRangeFilter, constraintValidatorContext);

      case stringEquals:
      case contains:

      default:
        return true;
    }
  }

  /**
   * Test case for Time Range.
   * <pre>
   * 1. we allow either left or right to be null but not both.
   * 2. left cannot be greater than right.
   * </pre>
   *
   * @param timeRangeFilter            {@linkplain TimeRangeFilter}
   * @param constraintValidatorContext {@linkplain ConstraintValidatorContext}
   * @return validation result.
   */
  private boolean validateTimeRangeFilter(TimeRangeFilter timeRangeFilter, ConstraintValidatorContext constraintValidatorContext) {
    if (timeRangeFilter.getRange() == null
            || (timeRangeFilter.getRange().getLeft() == null && timeRangeFilter.getRange().getRight() == null)) {
      overrideDefaultConstraintViolation(constraintValidatorContext, "type",
                                         TimeRangeFilter.startOrEndNotSpecifiedForTimeRange);
      return false;
    }
    if (timeRangeFilter.getRange().getLeft() != null && timeRangeFilter.getRange().getRight() != null
            && timeRangeFilter.getRange().getRight().isBefore(timeRangeFilter.getRange().getLeft())) {
      overrideDefaultConstraintViolation(constraintValidatorContext, "type",
                                         TimeRangeFilter.startGreaterThanEndForTimeRange);
      return false;
    }
    return true;
  }


  /**
   * Method to override default invalid filter message with given error message.
   *
   * @param constraintValidatorContext {@linkplain ConstraintValidatorContext}
   * @param node                       invalid request field
   * @param message                    invalid message
   */
  private void overrideDefaultConstraintViolation(ConstraintValidatorContext constraintValidatorContext, String node, String message) {
    constraintValidatorContext.disableDefaultConstraintViolation();
    constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                              .addPropertyNode(node).addConstraintViolation();
  }
}
