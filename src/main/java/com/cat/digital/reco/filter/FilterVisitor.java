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

package com.cat.digital.reco.filter;

import com.cat.digital.reco.exceptions.ValidationException;
import com.cat.digital.reco.filter.type.BooleanFilter;
import com.cat.digital.reco.filter.type.ContainsFilter;
import com.cat.digital.reco.filter.type.IntegerEqualsFilter;
import com.cat.digital.reco.filter.type.StringEqualsFilter;
import com.cat.digital.reco.filter.type.TimeRangeFilter;

/**
 * The visitor interface as defined by the "visitor" pattern for all Filter types.
 * Concrete visitor implementations must derive from this interface.
 * <p></p>
 * This interface has default implementations for all filter types. The default implementation is to throw
 * an exception which indicates that this filter type is not supported.
 * Having a default implementation allows for users to implement only the filter types they support.
 * Also when new Filter Types are added, they need not implement them immediately.
 * <p></p>
 * This interface should be also be enhanced every time a new filter type is added.
 */
public interface FilterVisitor {

  String unsupportedFilterType = "Unsupported Filter Type: %s";

  /**
   * This method throws a {@link ValidationException} indicating that the filter type is unsupported.
   */
  default void throwExceptionForUnsupportedFilterType(final FilterType filterType) {
    throw new ValidationException(String.format(unsupportedFilterType, filterType.name()));
  }

  /**
   * This method supports visiting of a visitor for the String Equals filter type.
   * {@link StringEqualsFilter}
   */
  default void visit(final StringEqualsFilter stringEqualsFilter) {
    throwExceptionForUnsupportedFilterType(FilterType.stringEquals);
  }

  /**
   * This method supports visiting of a visitor for the Integer Equals filter type.
   * {@link IntegerEqualsFilter}
   */
  default void visit(final IntegerEqualsFilter integerEqualsFilter) {
    throwExceptionForUnsupportedFilterType(FilterType.integerEquals);
  }

  /**
   * This method supports visiting of a visitor for the Boolean filter type.
   * {@link IntegerEqualsFilter}
   */
  default void visit(final BooleanFilter booleanFilter) {
    throwExceptionForUnsupportedFilterType(FilterType.booleanFilter);
  }

  /**
   * This method supports visiting of a visitor for the Contains filter type.
   * {@link ContainsFilter}
   */
  default void visit(final ContainsFilter containsFilter) {
    throwExceptionForUnsupportedFilterType(FilterType.contains);
  }

  /**
   * This method supports visiting of a visitor for the Time Range filter type.
   * {@link TimeRangeFilter}
   */
  default void visit(final TimeRangeFilter timeRangeFilter) {
    throwExceptionForUnsupportedFilterType(FilterType.timeRange);
  }

}
