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

import javax.validation.constraints.NotEmpty;

import com.cat.digital.reco.filter.Filter;
import com.cat.digital.reco.filter.FilterType;
import com.cat.digital.reco.filter.FilterVisitor;
import com.cat.digital.reco.filter.validator.ValidFilter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a single filtering criteria. This filter will only select a row in case that string contains the specified search value.
 * Matches are case insensitive by default and can be made case sensitive by specifying true for the isCaseSensitive attribute.
 */

@JsonTypeName("boolean")
@ValidFilter
@Getter
@Setter
public class BooleanFilter extends Filter {

  public BooleanFilter() {
    super(FilterType.booleanFilter);
  }

  /**
   * Search value.
   */
  @NotEmpty(message = "Filter field 'value' cannot contain an empty value.")
  private boolean value;

  /**
   * This is the accept method of the "visitor" pattern. It's only task is to dispatch the call to the visitor for processing.
   * @param visitor for which we accept the dispatched call.
   */
  @Override
  public void accept(FilterVisitor visitor) {
    visitor.visit(this);
  }
}
