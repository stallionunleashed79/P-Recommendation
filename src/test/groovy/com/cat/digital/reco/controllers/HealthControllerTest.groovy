package com.cat.digital.reco.controller

import com.cat.digital.reco.controllers.HealthController
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = HealthController.class)
class HealthControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    def "when get is performed then the response has status 200 and content is 'RECO API instance is healthy'"() {
        given:
        String healthUri = "/v1/health";

        expect: "Status is 200 and the response is 'RECO API instance is healthy'"
        mvc.perform(
                MockMvcRequestBuilders
                        .get(healthUri)
                        .header(HttpHeaders.AUTHORIZATION, "something")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == "RECO API instance is healthy"
    }
}
