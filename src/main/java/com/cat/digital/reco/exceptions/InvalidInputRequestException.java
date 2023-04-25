package com.cat.digital.reco.exceptions;

import com.cat.digital.reco.common.CustomResponseCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputRequestException extends RuntimeException {
  private final CustomResponseCodes code;

  public  InvalidInputRequestException(CustomResponseCodes code, String message) {
    super(message);
    this.code = code;
  }
}
