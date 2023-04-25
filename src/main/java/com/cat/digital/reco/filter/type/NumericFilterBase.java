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
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 *Base model for all filters that deal with numeric values and
 *measurements. When user is trying to filter by some measurement value
 *API must know in which measurement units search value is the provided.
 *For example: odometer reading. User will be able to filter by values
 *defined in miles or km.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class NumericFilterBase extends Filter {
  public static final int MAX_PRECISION = 14;

  public static final String unsupportedUoMForFilter = "Unsupported UoM for Numeric filter.";

  protected NumericFilterBase(FilterType filterType) {
    super(filterType);
  }

}
