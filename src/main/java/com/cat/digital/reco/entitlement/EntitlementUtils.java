package com.cat.digital.reco.entitlement;

import static com.cat.digital.reco.common.Constants.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.common.RestAPIOperations;
import com.cat.digital.reco.dao.DealerDetailsDao;
import com.cat.digital.reco.domain.models.Authorization;
import com.cat.digital.reco.exceptions.AuthorizationException;
import com.cat.digital.reco.exceptions.DealerDetailException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Data
@Log4j2
@AllArgsConstructor
public class EntitlementUtils {

  /**
   * Fetch entitlements for logged in user for the API operation.
   *
   * @param encodedAuthHeader the encoded authorization header
   * @param request           the servlet request for the API
   * @return the entitlements / authorization for the logged in user
   * @throws AuthorizationException Exception thrown while building authorization
   * @throws DealerDetailException  Exception thrown while fetching dealer codes
   * @throws IOException            The exception that is thrown while talking to dealer services API
   */
  public Authorization getEntitlements(
      final String encodedAuthHeader,
      final HttpServletRequest request) throws AuthorizationException, DealerDetailException, IOException {
    var decodedAuthString = decodeAuthorizationString(encodedAuthHeader);
    return getAuthorization(encodedAuthHeader, request, decodedAuthString);
  }

  /**
   * Generates the decoded authorization string from the header.
   * @param encodedAuthHeader the encoded authorization header
   * @return the decoded authorization string
   * @throws AuthorizationException exception thrown during decoding
   */
  private String decodeAuthorizationString(final String encodedAuthHeader) throws AuthorizationException {
    if (StringUtils.isEmpty(encodedAuthHeader)) {
      log.warn("Received request with missing token.");
      throw new AuthorizationException(NOT_AUTHORIZED_ENTITLEMENT_TOKEN_MISSING_MSG,
          CustomResponseCodes.NOT_AUTHORIZED_ENTITLEMENT_TOKEN_MISSING, HttpServletResponse.SC_UNAUTHORIZED);
    }
    try {
      return new String(Base64.getDecoder().decode(encodedAuthHeader), StandardCharsets.UTF_8);
    } catch (Exception parseException) {
      log.warn("Received request with token impossible to decode. Encoded token: [{}]", encodedAuthHeader);
      throw new AuthorizationException(NOT_AUTHORIZED_ENTITLEMENT_TOKEN_INVALID_MSG,
          CustomResponseCodes.NOT_AUTHORIZED_ENTITLEMENT_TOKEN_INVALID, HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  /**
   * Builds the authorization object from the decoded authorization string.
   *
   * @param encodedAuthHeader the encoded authorization header
   * @param request           the servlet request for the operation
   * @param decodedAuthString the decoded authorization string
   * @return the authorization object
   * @throws AuthorizationException Exception thrown while building authorization
   */
  private Authorization getAuthorization(
      final String encodedAuthHeader,
      final HttpServletRequest request,
      final String decodedAuthString) throws AuthorizationException, IOException, DealerDetailException {
    var documentContext = JsonPath.parse(decodedAuthString);
    validateForRecommendationSection(documentContext);
    validateForRolesSection(documentContext, request);
    final List<String> catRecIds = validateCatrecIds(encodedAuthHeader, documentContext);
    final Set<String> partyNumbersFromHeader = extractPartyNumbersFromResponse(request, documentContext);
    return fetchDealerCodesForCATUser(request, documentContext, catRecIds.get(0), partyNumbersFromHeader);
  }

  /**
   * Checks if the logged in user is a dealer or CAT user and fetches dealer codes for region code of CAT user.
   *
   * @param request                The servlet request
   * @param documentContext                    The document context for the entitlement response
   * @param catRecId               The catrecid of the user
   * @param partyNumbersFromHeader The party numbers in the response as input to the method
   * @return The authorization for that catrecid that contains the dealer codes for the catrecid
   * @throws AuthorizationException Throw authorizationexception if there are no dealer codes returned for CAT user
   */
  private Authorization fetchDealerCodesForCATUser(final HttpServletRequest request,
                                                   final DocumentContext documentContext,
                                                   final String catRecId,
                                                   final Set<String> partyNumbersFromHeader) throws AuthorizationException, IOException, DealerDetailException {
    if (partyNumbersFromHeader.contains(CATALL) || partyNumbersFromHeader.contains(CAT)) {
      return new Authorization(catRecId, new HashSet<>(partyNumbersFromHeader), false);
    }
    else {
      return new Authorization(catRecId, new HashSet<>(partyNumbersFromHeader), true);
    }
  }

  /**
   * Constructs the path to the partyNumbers or regionCodes section of entitlements response based on CAT user or dealer.
   * @param request The HttpServletRequest object
   * @return The dynamically constructed path string to region codes or partyNumbers section of entitlements response.
   */
  private String getPartyNumbersPath(final HttpServletRequest request) {
    var entitlementsRole = request.getRequestURI().endsWith(SEARCH_PATH)
            ? RestAPIOperations.GET.getDescription() : RestAPIOperations.valueOf(request.getMethod().toUpperCase()).getDescription();
    return String.format("$..recommendations.%s.filterConditions..partyNumbers.[*]", entitlementsRole);
  }

  /**
   * Navigates he entitlement response JSON and extracts the party numbers from the response via the DocumentContext.
   *
   * @param request The servlet request
   * @param documentContext     The document context for the entitlement response
   * @return The set of party numbers in the filteredConditions portion of the entitlement response
   * @throws AuthorizationException Throws an authorizationexception if there is no party numbers in response
   */
  private Set<String> extractPartyNumbersFromResponse(final HttpServletRequest request, final DocumentContext documentContext) throws AuthorizationException {
    var partyNumbersPath = getPartyNumbersPath(request);
    final Set<String> partyNumbersFromHeader = new HashSet<>(documentContext.read(partyNumbersPath));
    if (partyNumbersFromHeader.isEmpty()) {
      log.warn(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_PARTY_MSG);
      throw new AuthorizationException(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_PARTY_MSG,
          CustomResponseCodes.NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_PARTY, HttpServletResponse.SC_UNAUTHORIZED);
    }
    return partyNumbersFromHeader;
  }

  /**
   * Validates that there is a recommendation section in the entitlement response or else throw an authorization exception.
   *
   * @param documentContext       The document context for the entitlement response from Apigee
   * @throws AuthorizationException Exception to throw if there is no recommendation section in response
   */
  private void validateForRecommendationSection(final DocumentContext documentContext) throws AuthorizationException {
    final String section = documentContext.read("$..recommendations").toString();
    if (section.equals("[{}]") || section.equals("[]")) {
      log.warn("Recommendations section not present IN entitlements header");
      throw new AuthorizationException(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_RECOMMENDATIONS_MSG,
          CustomResponseCodes.NOT_AUTHORIZED_ENTITLEMENT_HDR_MISSING_RECOMMENDATIONS, HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  /**
   * Validates that there is a roles section in the entitlement response or else throw an authorization exception.
   *
   * @param documentContext The document context for the entitlement response from Apigee
   * @throws AuthorizationException Exception to throw if there is no matching role in response
   */
  private void validateForRolesSection(final DocumentContext documentContext,
                                       final HttpServletRequest request) throws AuthorizationException {
    var entitlementsRole = request.getRequestURI().endsWith(SEARCH_PATH)
            ? RestAPIOperations.GET.getDescription() : RestAPIOperations.valueOf(request.getMethod().toUpperCase()).getDescription();
    var entitlementsRoleMatcher = (SECTION_REGEX_PREFIX).concat(entitlementsRole);
    final String section = documentContext.read(entitlementsRoleMatcher).toString();
    if (section.equals("[{}]") || section.equals("[]")) {
      log.warn(String.format("%s section not present IN entitlements header", entitlementsRole));
      throw new AuthorizationException(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_ROLE_MSG,
          CustomResponseCodes.NOT_AUTHORIZED_ENTITLEMENT_HDR_MISSING_ROLE,
          HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  /**
   * Validates that the caterecid(s) in the entitlement response from Apigee is not empty.
   *
   * @param encodedAuthHeader The encoded auth header in the response
   * @param documentContext               The document context for the entitlement response
   * @return The list of catrecids (if present)
   * @throws AuthorizationException Exception thrown if caterecids list is empty
   */
  private List<String> validateCatrecIds(final String encodedAuthHeader, final DocumentContext documentContext) throws AuthorizationException {
    final List<String> catrecids = documentContext.read("$.*.catrecid");
    if (Objects.isNull(catrecids) || catrecids.isEmpty()) {
      log.warn("Received request with token impossible to decode. Encoded token: [{}]", encodedAuthHeader);
      throw new AuthorizationException(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_CATRECID_MSG,
          CustomResponseCodes.NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_CATRECID, HttpServletResponse.SC_UNAUTHORIZED);
    }
    return catrecids;
  }

  /**
   * Determines if the user is authorized to perform the operation else throws exception.
   *
   * @param authorization The authorization input containing dealer codes
   * @param dealerCode    The dealer code of the recommendation
   * @throws AuthorizationException The authorization exception to throw
   */
  public void validatePartyNumbers(final Authorization authorization, final String dealerCode) throws AuthorizationException {
    if (!authorization.getAuthorizedDealers().contains(CATALL)
            && !authorization.getAuthorizedDealers().contains(CAT)
            && !authorization.getAuthorizedDealers().contains(dealerCode)) {
      throw new AuthorizationException(NOT_HEADER_FORBIDDEN_MSG, CustomResponseCodes.NOT_HEADER_FORBIDDEN, HttpServletResponse.SC_FORBIDDEN);
    }
  }

  /**
   * Validates the authorization to update a recommendation.
   *
   * @param authorization          The authorization input containing dealer codes
   * @param isDealerRecommendation True if the recommendation was created by a dealer or
   *                               false if it was created by CAT/CATALL User
   * @throws AuthorizationException The authorization exception to throw
   */
  public void validateUpdateAction(final Authorization authorization,
                                   final boolean isDealerRecommendation) throws AuthorizationException {
    if ((authorization.isDealer() && !isDealerRecommendation)
        || (!authorization.isDealer() && isDealerRecommendation)) {
      throw new AuthorizationException(NOT_HEADER_FORBIDDEN_MSG,
          CustomResponseCodes.NOT_HEADER_FORBIDDEN, HttpServletResponse.SC_FORBIDDEN);
    }
  }

  /**
   * Validates the authorization to view a recommendation.
   *
   * @param authorization          The authorization input containing dealer codes
   * @param isDealerRecommendation Indicates if the recommendation was created by a dealer
   * @throws AuthorizationException The authorization exception to throw
   */
  public void validateViewAction(final Authorization authorization,
                                 final boolean isDealerRecommendation) throws AuthorizationException {
    if (authorization.isDealer() && !isDealerRecommendation) {
      throw new AuthorizationException(NOT_HEADER_FORBIDDEN_MSG,
          CustomResponseCodes.NOT_HEADER_FORBIDDEN, HttpServletResponse.SC_FORBIDDEN);
    }
  }

}
