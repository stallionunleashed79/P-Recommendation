package com.cat.digital.reco.controllers

import com.cat.digital.reco.domain.models.TemplateProperties
import com.cat.digital.reco.service.TemplateService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static com.cat.digital.reco.common.Constants.X_CAT_ENTITLEMENTS_HEADER
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TemplatesController.class)
class TemplatesControllerTest extends Specification {

    private static baseUrl = "/recommendations/v1/"
    private static templateName = 'Template 1 - Default'

    @SpringBean
    TemplateService templateService = Mock()

    @Autowired
    private WebApplicationContext webAppContext

    private MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build()
    }

    def "get template details" () {
        given:
        List< TemplateProperties> templateProperties = new ArrayList<>()
        templateProperties.add(TemplateProperties.builder().build())

        1 * templateService.getTemplateDetails(_ as String) >>  templateProperties

        def req = MockMvcRequestBuilders.get(baseUrl + 'templates/' + templateName)
                .header(X_CAT_ENTITLEMENTS_HEADER, "")
                .accept(MediaType.APPLICATION_JSON)

        when:
        def result = mockMvc.perform(req)

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath("\$").isArray())
        result.andExpect(jsonPath("\$.length()").value(1))
    }
}
