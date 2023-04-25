package com.cat.digital.reco.controllers;

import static com.cat.digital.reco.common.Constants.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.amazonaws.http.HttpMethodName;

import com.cat.digital.reco.domain.requests.RecommendationPublishRequest;
import com.cat.digital.reco.service.PublishService;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/recommendations/v1/recommendations", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class PublishController {

  private final PublishService publishService;

  @Autowired
  public PublishController(PublishService publishService) {
    this.publishService = publishService;
  }

  @PostMapping("/{recommendationNumber}/publish")
  public ResponseEntity<Object> publishRecommendation(
      @NotNull @PathVariable(value = "recommendationNumber", required = true) final String recommendationNumber,
      @Valid @RequestBody RecommendationPublishRequest publishRequest) {
    log.info(String.format("%s %s %s", ENTRY_CALL_MSG, HttpMethodName.POST, PUBLISH_RECOMMENDATION), recommendationNumber);
    publishService.publishRecommendation(publishRequest, recommendationNumber);
    log.info(String.format("%s %s %s", SUCCESSFUL_CALL_MSG, HttpMethodName.POST, PUBLISH_RECOMMENDATION), recommendationNumber);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
