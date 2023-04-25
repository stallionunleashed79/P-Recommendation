package com.cat.digital.reco.controllers;

import static com.cat.digital.reco.common.Constants.*;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.amazonaws.http.HttpMethodName;
import com.cat.digital.reco.domain.models.Authorization;
import com.cat.digital.reco.domain.requests.RecommendationPostRequest;
import com.cat.digital.reco.domain.requests.RecommendationPutRequest;
import com.cat.digital.reco.domain.responses.*;
import com.cat.digital.reco.exceptions.AuthorizationException;
import com.cat.digital.reco.service.RecommendationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cat.digital.reco.domain.models.OrderByParam;
import com.cat.digital.reco.domain.models.RecommendationSortByParam;
import com.cat.digital.reco.domain.requests.RecommendationSearchRequest;
import com.cat.digital.reco.domain.responses.Recommendation;
import com.cat.digital.reco.domain.responses.Recommendations;
import com.cat.digital.reco.domain.responses.ResponseMetadata;
import com.amazonaws.util.CollectionUtils;

@RestController
@Log4j2
@RequestMapping(value = "recommendations/v1/recommendations", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecommendationsController {

  private final RecommendationService recommendationService;

  @Autowired
  public RecommendationsController(RecommendationService service) {

    this.recommendationService = service;
  }

  /**
   * The REST end point to create a recommendation.
   *
   * @param payload request body
   * @return the response from the create recommendation API
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RecommendationDetailsResponse> createRecommendation(
      @RequestBody @Valid RecommendationPostRequest payload,
      @RequestAttribute(name = "EntitledDealers") Authorization authorization) throws AuthorizationException {
    log.info(String.format(ENTRY_CALL_MSG, HttpMethodName.POST, CREATE_RECOMMENDATION));
    var recommendation = recommendationService.createRecommendation(payload,
        authorization);
    var locationHeader = String.format(LOCATION_HEADER_URL_PREFIX, recommendation.getRecommendationNumber());
    var response = ResponseEntity.created(
        URI.create(locationHeader)).body(recommendation);
    log.info(String.format(SUCCESSFUL_CALL_MSG, HttpMethodName.POST, CREATE_RECOMMENDATION));
    return response;
  }

  /**
   * The REST End point to update a recommendation.
   *
   * @param recommendationNumber     The unique identifier for the recommendation to be updated
   * @param recommendationPutRequest The request for the PUT recommendation end point
   * @return                         No content
   */
  @PutMapping(value = "/{recommendationNumber}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RecommendationDetailsResponse> updateRecommendation(
      @PathVariable(value = "recommendationNumber", required = true) final String recommendationNumber,
      @RequestBody @Valid final RecommendationPutRequest recommendationPutRequest,
      @RequestAttribute(name = "EntitledDealers") Authorization authorization) throws AuthorizationException {
    log.info(String.format(ENTRY_CALL_MSG, HttpMethodName.PUT, UPDATE_RECOMMENDATION), recommendationNumber);

    var recommendation = recommendationService.updateRecommendation(recommendationPutRequest,
        recommendationNumber, authorization);
    log.info(String.format(SUCCESSFUL_CALL_MSG, HttpMethodName.PUT, UPDATE_RECOMMENDATION), recommendationNumber);
    return ResponseEntity.ok(recommendation);
  }

  /**
   * Retrieve the recommendation details.
   *
   * @param recommendationNumber The unique identifier recommendation
   * @return recommendation details
   */
  @GetMapping(value = "/{recommendationNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RecommendationDetailsResponse> getRecommendationsDetails(
      @PathVariable final String recommendationNumber,
      @RequestAttribute(name = "EntitledDealers") Authorization authorization) throws AuthorizationException {

    log.info(String.format(ENTRY_CALL_MSG, HttpMethodName.GET, GET_RECOMMENDATION), recommendationNumber);
    RecommendationDetailsResponse recommendationDetails = recommendationService.getRecommendationDetails(recommendationNumber, authorization);
    log.info(String.format(SUCCESSFUL_CALL_MSG, HttpMethodName.PUT, GET_RECOMMENDATION), recommendationNumber);
    return ResponseEntity.ok(recommendationDetails);
  }

  /**
   * Download the PDF file for an specific recommendation.
   *
   * @param recommendationNumber The unique identifier recommendation
   * @return recommendation PDF
   */
  @GetMapping(value = "/{recommendationNumber}", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<InputStreamResource> getRecommendationDetailsPDF(
      @PathVariable final String recommendationNumber,
      @RequestAttribute(name = "EntitledDealers") Authorization authorization) throws AuthorizationException {
    log.info(String.format(ENTRY_CALL_MSG, HttpMethodName.GET, GET_RECOMMENDATION), recommendationNumber);
    InputStreamResource recommendationPDF = recommendationService.getRecommendationPDF(recommendationNumber, authorization);
    log.info(String.format(SUCCESSFUL_CALL_MSG, HttpMethodName.PUT, GET_RECOMMENDATION), recommendationNumber);
    //TODO change the filename
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=Recommendation_%s.pdf", recommendationNumber)).body(recommendationPDF);
  }

  /**
   * Retrieve list of recommendation based on the filters.
   *
   * @param sortBy                      parameter to facilitate sorting of recommmendation based on the value e.g. serialNumber
   * @param orderBy                     parameter to facilitate ordering of recommendation e.g. ASC
   * @param cursor                      String token that can be used to retrieve next query results page. Skip this parameter to load the first page.
   * @param limit                       Number of records to return in a single page.
   * @param skip                        Number of records to skip before returning the next page. In case cursor is provided records will be skipped starting from that cursor position.
   * @param searchValue                 Additional filter parameter to refine the search based on the following recommendations properties.
   * @param recommendationSearchRequest List of filtering criteria to narrow down result set.
   * @return List of Recommendations based on the filtering criteria
   */
  @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Recommendations> getRecommendationSearch(
      @RequestParam final List<String> sortBy,
      @RequestParam final List<OrderByParam> orderBy,
      @RequestParam final String cursor,
      @RequestParam final String limit,
      @RequestParam final String skip,
      @RequestParam final String searchValue,
      @RequestBody @Valid final RecommendationSearchRequest recommendationSearchRequest,
      @RequestAttribute(name = "EntitledDealers") Authorization authorization) throws AuthorizationException {
    log.info(String.format(ENTRY_CALL_MSG, HttpMethodName.POST, SEARCH_RECOMMENDATIONS));
    var sortByEnum = sortBy.stream().map(RecommendationSortByParam::findByName).collect(Collectors.toList());
    if (sortByEnum.stream().anyMatch(Objects::isNull)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    final List<Recommendation> recommendationList = recommendationService.getRecommendations(authorization, sortByEnum, orderBy,
        recommendationSearchRequest);
    if (CollectionUtils.isNullOrEmpty(recommendationList)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    ResponseMetadata responseMetadata = ResponseMetadata.builder().nextCursor("next").build();
    Recommendations recommendations = Recommendations.builder().recommendations(recommendationList)
        .responseMetadata(responseMetadata).build();
    log.info(String.format(SUCCESSFUL_CALL_MSG, HttpMethodName.POST, SEARCH_RECOMMENDATIONS));
    return ResponseEntity.ok(recommendations);
  }

  /**
   * Export recommendation based on the filters.
   *
   * @param sortBy                      parameter to facilitate sorting of recommmendation based on the value e.g. serialNumber
   * @param orderBy                     parameter to facilitate ordering of recommendation e.g. ASC
   * @param cursor                      String token that can be used to retrieve next query results page. Skip this parameter to load the first page.
   * @param limit                       Number of records to return in a single page.
   * @param skip                        Number of records to skip before returning the next page. In case cursor is provided records will be skipped starting from that cursor position.
   * @param searchValue                 Additional filter parameter to refine the search based on the following recommendations properties.
   * @param recommendationSearchRequest List of filtering criteria to narrow down result set.
   * @return Export recommendation based on the filtering criteria
   */
  @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<InputStreamResource> getRecommendationSearchExport(
          @RequestParam final List<String> sortBy,
          @RequestParam final List<OrderByParam> orderBy,
          @RequestParam final String cursor,
          @RequestParam final String limit,
          @RequestParam final String skip,
          @RequestParam final String searchValue,
          @RequestBody @Valid final RecommendationSearchRequest recommendationSearchRequest,
          @RequestAttribute(name = "EntitledDealers") Authorization authorization) throws AuthorizationException, IOException {
    log.info(String.format(ENTRY_CALL_MSG, HttpMethodName.POST, EXPORTS_RECOMMENDATIONS));
    var sortByEnum = sortBy.stream().map(RecommendationSortByParam::findByName).collect(Collectors.toList());
    if (sortByEnum.stream().anyMatch(Objects::isNull)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    final InputStreamResource recommendationInputStream = recommendationService.getRecommendationsExport(authorization, sortByEnum, orderBy, recommendationSearchRequest);
    log.info(String.format(SUCCESSFUL_CALL_MSG, HttpMethodName.POST, EXPORTS_RECOMMENDATIONS));
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=Recommendation_Export.csv").body(recommendationInputStream);
  }
}
