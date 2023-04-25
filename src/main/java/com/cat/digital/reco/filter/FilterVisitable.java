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

/**
 * The visitable interface as defined by the "visitor" pattern for all Filter types.
 */
public interface FilterVisitable {

  /**
   * This is the accept method of the "visitor" pattern. It's only task is to dispatch the call to the visitor for processing.
   * @param visitor for which we accept the dispatched call.
   */
  void accept(FilterVisitor visitor);
}
