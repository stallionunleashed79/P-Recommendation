package com.cat.digital.reco.service.impl;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse;
import com.cat.digital.reco.exceptions.RecoServerException;
import com.cat.digital.reco.service.PDFGenerator;
import com.itextpdf.text.DocumentException;
import org.springframework.core.io.InputStreamResource;

public interface PDFGeneratorBuilder extends PDFGenerator {

  String setupXSLTemplate(String templateName);

  String createXML(RecommendationDetailsResponse recommendationDetailsResponse, String templateName) throws IOException;

  byte[] createHtml(String xmlPath, String xslPath) throws IOException, TransformerException;

  ByteArrayInputStream convertHtmlToPdf(byte[] html) throws IOException, DocumentException;

  default InputStreamResource generatePDF(String templateName, RecommendationDetailsResponse response) throws IOException, TransformerException  {
    // step 1
    String xslPath = this.setupXSLTemplate(templateName);

    // step 2
    String xmlPath = this.createXML(response, templateName);

    // step 3
    byte[] html = this.createHtml(xmlPath, xslPath);

    // step 4
    try {
      ByteArrayInputStream pdfDocument = this.convertHtmlToPdf(html);
      return new InputStreamResource(pdfDocument);
    } catch (Exception ex) {
      throw new RecoServerException(CustomResponseCodes.INTERNAL_SERVER_ERROR, "Error trying to generate pdf");
    }

  }



}
