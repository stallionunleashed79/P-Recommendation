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

package com.cat.digital.reco.filter.type;

import com.cat.digital.reco.filter.Filter;
import com.cat.digital.reco.filter.FilterType;
import com.cat.digital.reco.filter.FilterVisitor;
import com.cat.digital.reco.filter.validator.ValidFilter;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.Setter;


/**
 * Allows to filter time values within a certain range. Range is defined
 * by 'left' and 'right' parameters. If one of the boundaries is omitted or
 * null, it will be assumed that range is open bounded (infinity on that
 * side). In addition, two boolean flags indicate whether boundaries are
 * included in the range. Also 'left' should always be less than or equal
 * to 'right'.
 */

@JsonTypeName("timeRange")
@ValidFilter
@Getter
@Setter
public class TimeRangeFilter extends Filter {

  public static final String startGreaterThanEndForTimeRange
          = "'left' should be less than or equal to 'right' for timeRange filter.";

  public static final String startOrEndNotSpecifiedForTimeRange
          = "At least one of 'left' or 'right' or both should be specified for timeRange filter.";

  public TimeRangeFilter() {
    super(FilterType.timeRange);
  }

  TimeRange range = null;

  /**
   * This is the accept method of the "visitor" pattern. It's only task is to dispatch the call to the visitor for processing.
   * @param visitor for which we accept the dispatched call.
   */
  @Override
  public void accept(FilterVisitor visitor) {
    visitor.visit(this);
  }
}
