package com.cat.digital.reco.exceptions;

import com.cat.digital.reco.common.CustomResponseCodes;
import lombok.Getter;

@Getter
public class UnsupportedDataTypeException extends RuntimeException {

  private final CustomResponseCodes code;

  public UnsupportedDataTypeException(String message, CustomResponseCodes code) {
    super(message);
    this.code = code;
  }
}
