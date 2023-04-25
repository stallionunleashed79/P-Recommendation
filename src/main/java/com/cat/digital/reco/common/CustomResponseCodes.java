package com.cat.digital.reco.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomResponseCodes {

  BAD_REQUEST_INVALID_BODY_INPUT("400.102"),
  BAD_REQUEST_MISSING_REQUIRED_FIELDS("400.006"),
  BAD_REQUEST_MISSING_REQUIRED_REQUEST_BODY("400.008"),
  NOT_FOUND("404.000"),
  REACHED_MAX_RETRY_FOR_GENERATE_RECOMMENDATION_NUMBER("500.101"),
  INTERNAL_SERVER_ERROR("500.001"),
  //401
  NOT_AUTHORIZED_ENTITLEMENT_TOKEN_MISSING("401.101"),
  NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_CATRECID("401.102"),
  NOT_AUTHORIZED_ENTITLEMENT_TOKEN_INVALID("401.103"),
  NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_PARTY("401.104"),
  NOT_AUTHORIZED_ENTITLEMENT_HDR_MISSING_RECOMMENDATIONS("401.105"),
  NOT_AUTHORIZED_REQUEST_MAXIMUM_REACH("401.105"),
  NOT_AUTHORIZED_REQUEST_EXCEPTION("401.106"),
  //403
  NOT_HEADER_FORBIDDEN("403.101"),
  NOT_AUTHORIZED_ENTITLEMENT_HDR_MISSING_ROLE("403.101"),
  NOT_RESOLVABLE_DEALER_FORBIDDEN("403.102");

  String code;
}

