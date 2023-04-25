package com.cat.digital.reco.exceptions;

import com.cat.digital.reco.common.CustomResponseCodes;
import lombok.Getter;

@Getter
public class RecommendationNumberException extends RuntimeException {

  private final CustomResponseCodes code;

  public RecommendationNumberException(String message, CustomResponseCodes code) {
    super(message);
    this.code = code;
  }
}
