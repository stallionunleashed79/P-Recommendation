package com.cat.digital.reco.exceptions;

import static com.cat.digital.reco.common.Constants.ERROR_CALL_MSG;
import static com.cat.digital.reco.common.Constants.HTTP_ERROR_MESSAGE_MISSING_FIELDS;

import java.util.ArrayList;
import java.util.List;

import com.cat.digital.reco.common.Constants;
import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.domain.models.AdditionalInfo;
import com.cat.digital.reco.domain.responses.RecoError;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter
   * is missing.
   *
   * @param ex      MissingServletRequestParameterException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    return buildResponseEntity(new RecoError(HttpStatus.BAD_REQUEST, String.format("%s  parameter is missing", ex.getParameterName())));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex) {
    return buildResponseEntity(new RecoError(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(InvalidInputRequestException.class)
  public ResponseEntity<Object> handleInvalidRequestException(final InvalidInputRequestException ex) {
    return buildResponseEntity(new RecoError(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(AuthorizationException.class)
  public ResponseEntity<Object> handleAuthorizationException(final AuthorizationException ex) {
    return buildResponseEntity(new RecoError(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(DealerDetailException.class)
  public ResponseEntity<Object> handleDealerDetailException(final DealerDetailException ex) {
    return buildResponseEntity(new RecoError(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(RecoServerException.class)
  public ResponseEntity<Object> handleRecoServerException(final RecoServerException ex) {
    return buildResponseEntity(new RecoError(ex.getCode(), ex.getMessage()));
  }

  @ExceptionHandler(UnsupportedDataTypeException.class)
  public ResponseEntity<Object> handleUnsupportedDataTypeException(UnsupportedDataTypeException ex) {
    return buildResponseEntity(new RecoError(ex.getCode(), ex.getMessage()));
  }

  /**
   * Handle all errors related with recommendation number, like generation.
   *
   * @param ex  RecommendationNumberException
   * @return    the ApiError object
   */
  @ExceptionHandler(RecommendationNumberException.class)
  public ResponseEntity<Object> handleRecommendationNumberException(RecommendationNumberException ex) {
    return buildResponseEntity(new RecoError(ex.getCode(), ex.getMessage()));
  }

  private ResponseEntity<Object> buildResponseEntity(RecoError apiError) {
    log.error(ERROR_CALL_MSG, apiError.getDescription());
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  /**
   * Handle MethodArgumentNotValidException. Triggered when a 'required' fields of the request body
   * is missing.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                final HttpHeaders headers,
                                                                final HttpStatus status,
                                                                final WebRequest request) {
    log.info("Handling bad request for handleMethodArgumentNotValid.....");
    final List<AdditionalInfo> additionalInfoList = new ArrayList<AdditionalInfo>();

    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      additionalInfoList.add(new AdditionalInfo(error.getField(), error.getDefaultMessage(), Constants.RECO_API_SUBCODE));
    }
    return buildResponseEntity(new RecoError(CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_FIELDS, HTTP_ERROR_MESSAGE_MISSING_FIELDS, additionalInfoList));
  }

  /**
   * Handle MethodArgumentNotValidException. Triggered when a 'required' fields of the request body
   * is missing in the dynamic part.
   */
  @ExceptionHandler(CustomMethodArgumentNotValidException.class)
  protected ResponseEntity<Object> customHandleMethodArgumentNotValid(final CustomMethodArgumentNotValidException ex) {
    log.info("Handling bad request for customHandleMethodArgumentNotValid.....");
    final List<AdditionalInfo> additionalInfoList = new ArrayList<AdditionalInfo>();

    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      additionalInfoList.add(new AdditionalInfo(error.getField(), error.getDefaultMessage(), Constants.RECO_API_SUBCODE));
    }
    return buildResponseEntity(new RecoError(CustomResponseCodes.BAD_REQUEST_MISSING_REQUIRED_FIELDS, HTTP_ERROR_MESSAGE_MISSING_FIELDS, additionalInfoList));
  }


}

