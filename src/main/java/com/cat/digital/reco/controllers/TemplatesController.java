package com.cat.digital.reco.controllers;

import static com.cat.digital.reco.common.Constants.*;

import java.util.List;

import com.amazonaws.http.HttpMethodName;
import com.cat.digital.reco.domain.models.TemplateProperties;
import com.cat.digital.reco.service.TemplateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value = "recommendations/v1/templates", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TemplatesController {

  private final TemplateService templateService;

  public TemplatesController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @GetMapping(value = "{templateName}")
  public ResponseEntity<List<TemplateProperties>> getTemplateDetails(@PathVariable(value = "templateName") String templateName) {
    log.info(String.format(ENTRY_CALL_MSG, HttpMethodName.GET, GET_TEMPLATE), templateName);
    var templateProperties = templateService.getTemplateDetails(templateName);
    log.info(String.format(SUCCESSFUL_CALL_MSG, HttpMethodName.PUT, GET_TEMPLATE), templateName);
    return ResponseEntity.ok(templateProperties);
  }
}
