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

import com.cat.digital.reco.filter.FilterType;
import com.cat.digital.reco.filter.FilterVisitor;
import com.cat.digital.reco.filter.validator.ValidFilter;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a single filtering criteria. This filter will only select a row in case that string contains the specified search value.
 * Matches are case insensitive by default and can be made case sensitive by specifying true for the isCaseSensitive attribute.
 */

@JsonTypeName("contains")
@ValidFilter
public class ContainsFilter extends StringFilterBase {

  public ContainsFilter() {
    super(FilterType.contains);
  }

  /**
   * This is the accept method of the "visitor" pattern. It's only task is to dispatch the call to the visitor for processing.
   * @param visitor for which we accept the dispatched call.
   */
  @Override
  public void accept(FilterVisitor visitor) {
    visitor.visit(this);
  }
}
