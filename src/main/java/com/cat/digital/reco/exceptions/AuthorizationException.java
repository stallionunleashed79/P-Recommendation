package com.cat.digital.reco.exceptions;

import com.cat.digital.reco.common.CustomResponseCodes;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorizationException extends Exception {

  private CustomResponseCodes code;
  private int httpResponseCode;

  public AuthorizationException(final String errorMessage, final CustomResponseCodes code,
                                final int httpResponseCode) {
    super(errorMessage);
    this.code = code;
    this.httpResponseCode = httpResponseCode;
  }
}
