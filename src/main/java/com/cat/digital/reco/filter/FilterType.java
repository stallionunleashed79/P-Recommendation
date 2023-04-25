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

import com.cat.digital.reco.filter.type.*;

/**
 * Allowed values for Filter and its matching Filter sub class type.
 *
 * @see Filter
 */
public enum FilterType {
  stringEquals(StringEqualsFilter.class),
  integerEquals(IntegerEqualsFilter.class),
  contains(ContainsFilter.class),
  timeRange(TimeRangeFilter.class),
  booleanFilter(BooleanFilter.class);

  private Class classType;

  /**
   * Assign filter type class based on enum value.
   *
   * @param classType filter sub type class.
   */
  FilterType(Class classType) {
    this.classType = classType;
  }

  /**
   * Getter for classType property.
   *
   * @return filter sub type class.
   */
  public Class getClassType() {
    return classType;
  }
}
