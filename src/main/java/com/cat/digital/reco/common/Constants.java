package com.cat.digital.reco.common;

public interface Constants {

  //LOG MESSAGE
  String ERROR_CALL_MSG = "Error message: {}";
  String ENTRY_CALL_MSG = ">>> Received request for %s %s";

  //AUTHORIZATION
  String CATALL = "CATALL";
  String CAT = "CAT";
  String SUCCESSFUL_CALL_MSG = "<<< Successfully processed request for %s %s";

  //LOG ENDPOINT NAMES
  String PUBLISH_RECOMMENDATION = "publish recommendation for the recommendation number [{}].";
  String CREATE_RECOMMENDATION = "create recommendation";
  String UPDATE_RECOMMENDATION = "update recommendation for the recommendation number [{}]";
  String GET_RECOMMENDATION = "get recommendation details for the recommendation number [{}]";
  String GET_TEMPLATE = "get template details for the template name [{}]";
  String DOUBLE_REGEX = "^\\d+\\.\\d{2}$";
  String ASSET_ID_DELIMITER = "\\|";
  String INVALID_TEMPLATE_FIELDS_ERR_MSG = "One of the template fields does not comply with standards";
  String SEARCH_RECOMMENDATIONS = "search recommendations";
  String EXPORTS_RECOMMENDATIONS = "export recommendations";

  //ENTITLEMENTS - 401 ERRORS
  String NOT_AUTHORIZED_ENTITLEMENT_TOKEN_MISSING_MSG = "Entitlement token missing.";
  String NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_CATRECID_MSG = "Invalid entitlement header. No catrecId found.";
  String NOT_AUTHORIZED_ENTITLEMENT_TOKEN_INVALID_MSG = "Invalid entitlement token.";
  String NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_PARTY_MSG = "Invalid entitlement header. No partyNumbers found.";
  String NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_RECOMMENDATIONS_MSG = "Invalid entitlement header. No recommendations entitlement found.";
  String NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_ROLE_MSG = "Invalid entitlement header. No matching role found.";
  String NOT_AUTHORIZED_DEALER_REQUEST_EXCEPTION_MSG = "Exception occurred when requesting dealer codes.";
  //403
  String NOT_HEADER_FORBIDDEN_MSG = "Forbidden, User doesn't have permissions to perform the operation.";
  String NOT_RESOLVABLE_DEALER_FORBIDDEN_MSG = "No resolvable dealer provided.";
  String NO_SORT_FILTER_RECOMMENDATION = "No recommendations pass the sort and/or search filters";

  //EMAIL VALIDATORS CONSTANTS
  String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
  String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)+";
  String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

  String EMAIL_PATTERN =
      "^" + ATOM + "+(\\." + ATOM + "+)*@"
          + DOMAIN
          + "|"
          + IP_DOMAIN
          + ")$";

  // AUTHORIZATION HEADER
  String X_CAT_ENTITLEMENTS_HEADER = "x-cat-entitlements";

  // ACCEPT HEADER
  String ACCEPT = "Accept";

  // HTTP ERROR MESSAGES
  String HTTP_ERROR_MESSAGE_MISSING_FIELDS = "Invalid or missing request body.";

  // CUSTOM BUSINESS ERRORS
  String TEMPLATE_WAS_NOT_FOUND = "Template %s was not found.";
  String ASSET_WAS_NOT_FOUND = "Asset %s was not found.";
  String ASSET_IS_NOT_ENROLLED = "Asset %s is not enrolled.";
  String ASSET_INVALID_FORMAT = "Asset identifier %s is not valid.";

  // CUSTOM SUBCODES ERRORS
  String RECO_API_SUBCODE = "RECO_API_VALIDATION";

  String SECTION_REGEX_PREFIX = "$..";
  String RECOMMENDATIONS_HEALTH_CHECK_URL_REGEX = "/health";

  // PAGINATION QUERY PARAM
  String SORT_BY = "sortBy";
  String ORDER_BY = "orderBy";
  String CURSOR = "cursor";
  String LIMIT = "limit";
  String SKIP = "skip";

  // SEARCH VALUE
  String SEARCH_VALUE = "searchValue";

  // pdf generator
  String XST_EXTENSION = "xsl";
  String LOCATION_HEADER_URL_PREFIX = "/recommendations/%s";

  // ENDPOINT PATH
  String SEARCH_PATH = "search";

  // SEARCH FILTER
  String INVALID_SEARCH_FILTER_FIELD = "Invalid search filter request, search filter fields are invalid";
}
