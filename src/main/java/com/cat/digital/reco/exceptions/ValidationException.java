/*
 *  Copyright (c) 2019 Caterpillar Inc. All Rights Reserved.
 *
 *  This work contains Caterpillar Inc.'s unpublished
 *  proprietary information which may constitute a trade secret
 *  and/or be confidential. This work may be used only for the
 *  purposes for which it was provided, and may not be copied
 *  or disclosed to others. Copyright notice is precautionary
 *  only, and does not imply publication.
 */

package com.cat.digital.reco.exceptions;

import lombok.Getter;

/**
 * ValidationException should be used in any case where a validation error occurs,
 * and you want to pass a custom message on to the user.
 * For example, when validating user input in a controller.
 * NOTE: the message used to create this exception WILL be passed directly on to the user by the
 * {@link GlobalExceptionHandler}
 * so use with caution.
 */
@Getter
public class ValidationException extends RuntimeException {
  private Boolean messageIsParameterName = false;

  public ValidationException(final String message) {
    super(message);
  }
}
