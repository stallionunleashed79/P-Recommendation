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

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for defining range input models.
 */

@Getter
@Setter
public class RangeBase {

  /**
   * Indicates whether left boundary of the range should be included.
   */
  private boolean includeLeft = true;

  /**
   * Indicates whether right boundary of the range should be included.
   */
  private boolean includeRight = true;

}
