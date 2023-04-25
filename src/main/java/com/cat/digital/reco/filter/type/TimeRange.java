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

import java.time.OffsetDateTime;

import lombok.*;

/**
 * Range is defined y 'left' and 'right' parameters. If one of the
 * boundaries is omitted or null, it will be assumed that range is open
 * bounded (infinity on that side). In addition, two boolean flags indicate
 * whether boundaries are included in the range. Also 'left' should always
 * be less than or equal to 'right'.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeRange extends RangeBase {

  /**
   * Left boundary of the interval.
   */
  private OffsetDateTime left;

  /**
   * Right boundary of the interval.
   */
  private OffsetDateTime right;
}
