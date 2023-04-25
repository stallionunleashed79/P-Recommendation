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
import java.util.List;

import com.cat.digital.reco.filter.Filter;
import com.cat.digital.reco.filter.FilterType;
import com.cat.digital.reco.filter.FilterVisitor;
import com.cat.digital.reco.filter.validator.ValidFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a single filtering criteria, i.e. name of the object field to filter by and list of possible values. This filter will only
 * select a row in case of EXACT MATCH.
 * Matches are case insensitive by default and can be made case sensitive by specifying true for the isCaseSensitive attribute.
 */

@JsonTypeName("stringEquals")
@ValidFilter
@Getter
@Setter
@SuperBuilder
public class StringEqualsFilter extends Filter {

  public StringEqualsFilter() {
    super(FilterType.stringEquals);
  }

  /**
   * Array of possible filter values.
   */
  @NotEmpty(message = "Filter field 'values' cannot be empty for type 'stringEquals'.")
  private List<@NotEmpty(message = "Filter field 'values' cannot contain empty values.") String> values;

  /**
   * Defines whether string comparison is case sensitive.
   */
  @JsonProperty(value = "isCaseSensitive")
  private boolean isCaseSensitive = false;

  /**
   * This is the accept method of the "visitor" pattern. It's only task is to dispatch the call to the visitor for processing.
   * @param visitor for which we accept the dispatched call.
   */
  @Override
  public void accept(FilterVisitor visitor) {
    visitor.visit(this);
  }
}
