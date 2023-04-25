package com.cat.digital.reco.service;

import javax.xml.transform.TransformerException;
import java.io.IOException;

import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse;
import org.springframework.core.io.InputStreamResource;

public interface PDFGenerator {

  InputStreamResource generatePDF(String templateName, RecommendationDetailsResponse model) throws IOException, TransformerException;
}

