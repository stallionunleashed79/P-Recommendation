package com.cat.digital.reco.interceptors

import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationInterceptorTest extends Specification {

    HttpServletRequest request = Mock()

    HttpServletResponse response = Mock()

    FilterChain filter = Mock()

    static def authorizationHeader = "Bearer any chain because only verify if the header is present"

    def interceptor = new AuthenticationInterceptor()

    def setup() {

    }

    def "request with header authorization"() {
        given:
        1 * request.getHeader(HttpHeaders.AUTHORIZATION) >> authorizationHeader

        when:
        interceptor.doFilterInternal(request, response, filter)

        then:
        0 * response.sendError(_ as Integer)
        1 * filter.doFilter(request, response)
        assert SecurityContextHolder.getContext().getAuthentication() != null
    }

    def "request without header authorization"() {
        given:
        1 * request.getHeader(HttpHeaders.AUTHORIZATION) >> null

        when:
        interceptor.doFilterInternal(request, response, filter)

        then:
        1 * response.sendError(_ as Integer)
        0 * filter.doFilter(request, response)
        assert SecurityContextHolder.getContext().getAuthentication() == null
    }
}
