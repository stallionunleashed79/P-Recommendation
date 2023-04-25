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
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an abstract base class for a single filtering criteria. This filter will only select a row in case that string matches with the specified value
 * via conditions in: {@link ContainsFilter}.
 * sensitive by specifying true for the isCaseSensitive attribute.
 */

@Getter
@Setter
public abstract class StringFilterBase extends Filter {

  protected StringFilterBase(FilterType filterType) {
    super(filterType);
  }

  /**
   * Search string.
   */
  @NotEmpty(message = "Filter field 'value' cannot contain an empty value.")
  private String value;

  /**
   * Defines whether string comparison is case sensitive.
   */
  @JsonProperty(value = "isCaseSensitive")
  private boolean isCaseSensitive = false;

}
