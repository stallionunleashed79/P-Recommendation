package com.cat.digital.reco.exceptions;

import com.cat.digital.reco.common.CustomResponseCodes;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

  private final CustomResponseCodes code;
  
  public  EntityNotFoundException(CustomResponseCodes code, String message) {
    super(message);
    this.code = code;
  }
}
