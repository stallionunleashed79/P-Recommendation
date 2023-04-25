package com.cat.digital.reco.service;

import java.util.List;

import com.cat.digital.reco.domain.models.TemplateProperties;

public interface TemplateService {

  List<TemplateProperties> getTemplateDetails(String templateName);
}
