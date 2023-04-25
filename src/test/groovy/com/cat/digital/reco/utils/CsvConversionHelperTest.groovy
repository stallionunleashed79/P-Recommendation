package com.cat.digital.reco.utils

import com.cat.digital.reco.domain.responses.RecommendationExport
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import spock.lang.Specification

@WebMvcTest(CsvConversionHelper.class)
class CsvConversionHelperTest extends Specification {

    CsvConversionHelper csvConversionHelper;

    def setup() {
        csvConversionHelper = Mock(CsvConversionHelper.class);
    }

    def "test csv generation"() {
        given:
        List<RecommendationExport> recommendationExportList = Arrays.asList(
             RecommendationExport.builder()
                .serialNumber("1")
                .recommendationNumber("REC-000-000-000")
                .owner("Waste, Management")
                .site("").build())

        when:
        def result = csvConversionHelper.toCsv(recommendationExportList, ",")

        then:
        result != null
    }
}