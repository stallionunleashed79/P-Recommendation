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

import javax.validation.Valid;
import java.util.List;

import com.cat.digital.reco.filter.validator.NullOrNotEmptyList;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FilterableRequest<T extends Enum<?>> extends Filters {
  // list of attributes (columns) to include in the response
  @Valid
  @NullOrNotEmptyList(message = "Response attributes list must not be empty if included")
  List<T> responseAttributes;

  @Builder(builderMethodName = "filterRequestBuilder")
  public FilterableRequest(String logicalExpression, List<Filter> filters, List<T> responseAttributes) {
    super(logicalExpression, filters);
    this.responseAttributes = responseAttributes;
  }
}
