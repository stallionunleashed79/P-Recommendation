package com.cat.digital.reco.exceptions;

import com.cat.digital.reco.common.CustomResponseCodes;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomMethodArgumentNotValidException extends RuntimeException {

  private final CustomResponseCodes code;
  private final transient BindingResult bindingResult;

  public  CustomMethodArgumentNotValidException(CustomResponseCodes code, String message,
                                                BindingResult bindingResult) {
    super(message);
    this.code = code;
    this.bindingResult = bindingResult;
  }
}
