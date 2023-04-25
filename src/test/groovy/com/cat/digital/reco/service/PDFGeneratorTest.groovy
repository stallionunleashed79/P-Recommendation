package com.cat.digital.reco.service

import com.cat.digital.reco.domain.responses.RecommendationDetailsResponse
import com.cat.digital.reco.service.impl.PDFGeneratorServiceImpl
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import spock.lang.Specification


@WebMvcTest(PDFGenerator.class)
class PDFGeneratorTest extends Specification {

    static def recommendationNumber = "REC 123-456-789"
    static def assetId = "CAT|RXZ00353|2969412354"
    static def templateName = "Template1Default"

    @Autowired
    final ResourceLoader resourceLoader;

    @Autowired
    PDFGenerator pdfService

    PDFGeneratorServiceImpl pdfGeneratorService

    def setup() {
        pdfGeneratorService = new PDFGeneratorServiceImpl(resourceLoader, "classpath:template/")
    }

    def "Setup xsl file success"() {
        given:
        when:
        def result = pdfGeneratorService.setupXSLTemplate(templateName);
        then:
        assert result != null
    }

    def "Create xml content success" () {
        given:
        def reco = new RecommendationDetailsResponse();
        reco.setTemplateName(templateName)
        reco.setAssetId(assetId)
        when:
        def result = pdfGeneratorService.createXML(reco, templateName)
        then:
        assert result != null
    }

    def "Create html content before to generate the pdf"() {
        given:
            StringBuilder xml = new StringBuilder();
            xml.append("<RecommendationDetailsResponse>")
            xml.append("<recommendationNumber>REC-766-469-034</recommendationNumber>")
            xml.append("<templateName>Template 1 - Default</templateName>")
            xml.append("</RecommendationDetailsResponse>")

        when:
            def path = pdfGeneratorService.setupXSLTemplate(templateName);
            def result = pdfGeneratorService.createHtml(xml.toString(), path)
        then:

        assert result != null
    }

    def "Covert html content to generate the pdf"() {
        given:
        StringBuilder xml = new StringBuilder();
        xml.append("<RecommendationDetailsResponse>")
        xml.append("<recommendationNumber>REC-766-469-034</recommendationNumber>")
        xml.append("<templateName>Template 1 - Default</templateName>")
        xml.append("</RecommendationDetailsResponse>")
        when:
        def path = pdfGeneratorService.setupXSLTemplate(templateName);
        def html = pdfGeneratorService.createHtml(xml.toString(), path)
        def result = pdfGeneratorService.convertHtmlToPdf(html);
        then:
        assert result != null
    }

}
