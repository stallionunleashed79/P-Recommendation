package com.cat.digital.reco.service;

import java.util.List;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.domain.entities.RecommendationCommonDataEntity;
import com.cat.digital.reco.domain.models.Authorization;
import com.cat.digital.reco.domain.models.OrderByParam;
import com.cat.digital.reco.domain.models.RecommendationSortByParam;
import com.cat.digital.reco.domain.requests.RecommendationPostRequest;
import com.cat.digital.reco.domain.requests.RecommendationPutRequest;
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest;
import com.cat.digital.reco.domain.responses.Recommendation;
import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse;
import com.cat.digital.reco.exceptions.AuthorizationException;
import org.springframework.core.io.InputStreamResource;

public interface RecommendationService {

  RecommendationDetailsResponse getRecommendationDetails(final String recommendationNumber,
                                                         final Authorization authorization) throws AuthorizationException;


  InputStreamResource getRecommendationPDF(final String recommendationNumber, final Authorization authorization) throws AuthorizationException;

  /**
   * Create a recommendation from the input request.
   *
   * @param body the request body for the create
   * @return the response from the create operation
   */
  RecommendationDetailsResponse createRecommendation(final RecommendationPostRequest body,
                                                  final Authorization authorization) throws AuthorizationException;

  /**
   * Update a recommendation from the input request.
   *
   * @param recommendationPutRequest The request for the PUT recommendation end point
   * @param recommendationNumber     The unique identifier of the recommendation to be updated
   * @param authorization            The authorization of the logged in user containing the party numbers
   */
  RecommendationDetailsResponse updateRecommendation(final RecommendationPutRequest recommendationPutRequest,
                            final String recommendationNumber,
                            final Authorization authorization) throws AuthorizationException;

  /**
   * Method will be use to validate if the recommendation exist.
   *
   * @param recommendationNumber The unique identifier of there commendation to be lookup
   * @param errorCode            The status error code to be throw
   * @return RecommendationHeaderEntity The recommendation header object
   */
  RecommendationCommonDataEntity validateRecommendationExists(String recommendationNumber, CustomResponseCodes errorCode);

  /**
   * Method will used to return all the recommendations.
   *
   * @return The list of recommendations
   */
  List<Recommendation> getRecommendations(final Authorization authorization, final List<RecommendationSortByParam> sortByParams, final List<OrderByParam> orderByParams,
                                          final RecommendationSearchRequest recommendationSearchRequest) throws AuthorizationException;

  /**
   * Method will used to return all the recommendations as a CSV export.
   *
   * @return input stream with all recommendations
   */
  InputStreamResource getRecommendationsExport(final Authorization authorization, final List<RecommendationSortByParam> sortByParams, final List<OrderByParam> orderByParams, final RecommendationSearchRequest recommendationSearchRequest) throws AuthorizationException;
}
