package com.cat.digital.reco.exceptions;

import com.cat.digital.reco.common.CustomResponseCodes;
import lombok.NoArgsConstructor;

public class DealerDetailException extends Exception {

  private CustomResponseCodes code;

  public DealerDetailException(final String errorMessage) {
    super(errorMessage);
  }

  public DealerDetailException(final String errorMessage, final CustomResponseCodes code) {
    super(errorMessage);
    this.code = code;
  }

  public CustomResponseCodes getCode() {
    return code;
  }
}
