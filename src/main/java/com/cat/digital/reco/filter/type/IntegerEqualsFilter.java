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
import javax.validation.constraints.NotNull;
import java.util.List;

import com.cat.digital.reco.filter.FilterType;
import com.cat.digital.reco.filter.FilterVisitor;
import com.cat.digital.reco.filter.validator.ValidFilter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;


/**
 * Represents a single filtering criteria, i.e. name of the object field to filter by and list of possible values. This filter will select a row
 * only in case of match with one of the values in the provided list.
 */

@JsonTypeName("integerEquals")
@ValidFilter
@Getter
@Setter
public class IntegerEqualsFilter extends NumericFilterBase {

  public IntegerEqualsFilter() {
    super(FilterType.integerEquals);
  }

  /**
   * Array of possible filter values.
   */
  @NotEmpty(message = "Filter field 'filterValues' cannot be empty for type 'integerEquals'.")
  private List<@NotNull(message = "Filter field 'filterValues' cannot contain blank values.") Integer> values;

  /**
   * This is the accept method of the "visitor" pattern. It's only task is to dispatch the call to the visitor for processing.
   * @param visitor for which we accept the dispatched call.
   */
  @Override
  public void accept(FilterVisitor visitor) {
    visitor.visit(this);
  }
}
