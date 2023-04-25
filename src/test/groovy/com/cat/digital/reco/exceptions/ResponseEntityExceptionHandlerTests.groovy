package com.cat.digital.reco.exceptions

import com.cat.digital.reco.common.CustomResponseCodes
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.bind.MissingServletRequestParameterException

import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import spock.lang.Specification

import javax.servlet.http.HttpServletResponse

import static com.cat.digital.reco.common.Constants.NOT_AUTHORIZED_DEALER_REQUEST_EXCEPTION_MSG
import static com.cat.digital.reco.common.Constants.NOT_RESOLVABLE_DEALER_FORBIDDEN_MSG

class ResponseEntityExceptionHandlerTests extends Specification {

    private RestExceptionHandler handler

    private MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/");

    private MockHttpServletResponse servletResponse = new MockHttpServletResponse();

    private WebRequest request = new ServletWebRequest(this.servletRequest, this.servletResponse);

    def setup() {
        handler = new RestExceptionHandler();
    }

    def "test handle Missing Servlet Request Parameter Exception"() {
        given:
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("message", "trackId");

        HttpHeaders headers = new HttpHeaders()

        when:
        ResponseEntity<Object> responseEntity = handler.handleMissingServletRequestParameter(ex, headers, HttpStatus.BAD_REQUEST, this.request);

        then:
        assert responseEntity != null
    }

    def "test handle Entity Not Found Exception"() {
        given:
        EntityNotFoundException ex = new EntityNotFoundException(CustomResponseCodes.NOT_FOUND, "Entity not found")

        when:
        ResponseEntity<Object> response = handler.handleEntityNotFoundException(ex)

        then:
        assert response != null
    }

    def "test handle dealer detail Exception"() {
        given:
        DealerDetailException ex = new DealerDetailException(NOT_AUTHORIZED_DEALER_REQUEST_EXCEPTION_MSG,
                CustomResponseCodes.NOT_AUTHORIZED_REQUEST_EXCEPTION)

        when:
        ResponseEntity<Object> response = handler.handleDealerDetailException(ex)

        then:
        assert response != null
    }

    def "test handle authorization exception"() {
        given:
        AuthorizationException ex = new AuthorizationException(NOT_RESOLVABLE_DEALER_FORBIDDEN_MSG,
                CustomResponseCodes.NOT_RESOLVABLE_DEALER_FORBIDDEN, HttpServletResponse.SC_FORBIDDEN)

        when:
        ResponseEntity<Object> response = handler.handleAuthorizationException(ex)

        then:
        assert response != null
    }

    def "test handle Invalid Input Request Exception"() {
        given:
        InvalidInputRequestException ex = new InvalidInputRequestException(CustomResponseCodes.INTERNAL_SERVER_ERROR, "some message")

        when:
        ResponseEntity<Object> response = handler.handleInvalidRequestException(ex)

        then:
        assert response != null
    }

    def "test handle Reco Server Exception"() {
        given:
        RecoServerException ex = new RecoServerException(CustomResponseCodes.INTERNAL_SERVER_ERROR, "some message")

        when:
        ResponseEntity<Object> response = handler.handleRecoServerException(ex)

        then:
        assert response != null
    }

    def "test handle Unsupported Data Type Exception"() {
        given:
        UnsupportedDataTypeException ex = new UnsupportedDataTypeException("message", CustomResponseCodes.INTERNAL_SERVER_ERROR);

        when:
        ResponseEntity<Object> responseEntity = handler.handleUnsupportedDataTypeException(ex);

        then:
        assert responseEntity != null
    }

    def "test handle Recommendation Number Exception"() {
        given:
        RecommendationNumberException ex = new RecommendationNumberException("some message", CustomResponseCodes.INTERNAL_SERVER_ERROR)

        when:
        ResponseEntity<Object> response = handler.handleRecommendationNumberException(ex)

        then:
        assert response != null
    }
}
