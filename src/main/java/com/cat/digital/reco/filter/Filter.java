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

import javax.validation.constraints.NotNull;

import com.cat.digital.reco.filter.validator.ValidFilter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Filter object defines values applied to a specific filter key.
 */
@Getter
@Setter
@SuperBuilder
@ValidFilter
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CUSTOM,
    property = "type",
    include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonTypeIdResolver(CustomFilterTypeIdResolver.class)
public abstract class Filter implements FilterVisitable {

  public static final String propertyNameParameterIsNotPresent = "Required parameter 'propertyName' is not present.";

  protected Filter(FilterType filterType) {
    this.type = filterType;
  }

  /**
   * propertyName attribute can be null for {@see GroupFilter}.
   */
  private String propertyName = null;

  @NotNull(message = "Required parameter 'type' is not present.")
  private FilterType type;
}
