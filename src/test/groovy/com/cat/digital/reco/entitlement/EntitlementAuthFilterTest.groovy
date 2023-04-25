package com.cat.digital.reco.entitlement

import com.cat.digital.reco.domain.models.Authorization
import org.apache.http.impl.bootstrap.HttpServer
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import static com.cat.digital.reco.common.Constants.*;

class EntitlementAuthFilterTest extends Specification{

	EntitlementUtils entitlementUtils;
    HttpServletRequest request;

	def setup() {
		entitlementUtils = Mock(EntitlementUtils.class)
    request = Mock()
    request.getMethod() >> "POST"
    request.getRequestURI() >> "/abc"
	}

	def "Test Entitlement Auth Filter does not exist in RECO"(){

		EntitlementAuthFilter filter = new EntitlementAuthFilter();
		given:

		def servletRequest = Mock(HttpServletRequest.class);
		def servletResponse = Mock(HttpServletResponse.class)
		def filterChain = Mock(FilterChain.class)

		String entitlementToken  = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0zMUU5QjZERiIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJyZWNvbW1lbmRhdGlvbnMiOiB7CiAgICAgICAgInZpZXciOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVCIsICJCMDMwIgogICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgInJlZ2lvbnMiOiBbCiAgICAgICAgICAgICAgICAiQURTRC1OIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgImNyZWF0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiVDAzMCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJ1cGRhdGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVCIsICJCMDMwIgogICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgInJlZ2lvbnMiOiBbCiAgICAgICAgICAgICAgICAiQURTRC1OIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgImRlbGV0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQjAzMCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9CiAgICAgIH0KICAgIH0KICB9Cn0="
        servletRequest.getRequestURI() >> "/recommendations/v1/recommendations"
		servletRequest.getHeader("x-cat-entitlements") >> entitlementToken
		servletRequest.getMethod() >> "POST"
		filter.entitlementUtils = entitlementUtils
		1 * entitlementUtils.getEntitlements(entitlementToken, servletRequest)

		when:
		filter.doFilter(servletRequest, servletResponse, filterChain)

		then:
		1 * filterChain.doFilter(servletRequest, servletResponse)
	}

	def "Test Entitlement Auth Filter entitlement is valid in RECO"(){

		EntitlementAuthFilter filter = new EntitlementAuthFilter();
		given:

		def servletRequest = Mock(HttpServletRequest.class);
		def servletResponse = Mock(HttpServletResponse.class)
		def filterChain = Mock(FilterChain.class)
        servletRequest.getRequestURI() >> "/recommendations/v1/recommendations"
		Authorization authorization = new Authorization();
		String entitlementToken  = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0yRDI2QURDQSIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJDVkEiOiB7CiAgICAgICAgIkRFTEVURSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQ0FUIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgIlBPU1QiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIlREMDAiCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfSwKICAgICAgICAiUFVUIjogewogICAgICAgICAgImFsbG93ZWRGaWVsZHMiOiBbCiAgICAgICAgICAgICIqIgogICAgICAgICAgXSwKICAgICAgICAgICJmaWx0ZXJDb25kaXRpb25zIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgInBhcnR5TnVtYmVycyI6IFsKICAgICAgICAgICAgICAgICJDQVQiCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfSwKICAgICAgICAiR0VUIjogewogICAgICAgICAgImFsbG93ZWRGaWVsZHMiOiBbCiAgICAgICAgICAgICIqIgogICAgICAgICAgXSwKICAgICAgICAgICJmaWx0ZXJDb25kaXRpb25zIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgInBhcnR5TnVtYmVycyI6IFsKICAgICAgICAgICAgICAgICJURE9PIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0KICAgICAgfQogICAgfQogIH0sCiAgImFsZyI6ICJIUzI1NiIKfQ=="
		servletRequest.getHeader("x-cat-entitlements") >> entitlementToken
		servletRequest.getMethod() >> "POST"
		filter.entitlementUtils = entitlementUtils
		1 * entitlementUtils.getEntitlements(entitlementToken, servletRequest) >>  authorization

		when:
		filter.doFilter(servletRequest, servletResponse, filterChain)

		then:
		noExceptionThrown()
		1 * filterChain.doFilter(servletRequest, servletResponse)
	}

  def "Skip entitlement check for non-recommendation requests like health"(){

        EntitlementAuthFilter filter = new EntitlementAuthFilter();
        given:

        def servletRequest = Mock(HttpServletRequest.class);
        def servletResponse = Mock(HttpServletResponse.class)
        def filterChain = Mock(FilterChain.class)
        servletRequest.getRequestURI() >> "/health"
        servletRequest.getMethod() >> "GET"

        when:
        filter.doFilter(servletRequest, servletResponse, filterChain)
        then:
        noExceptionThrown()
		0 * entitlementUtils.getEntitlements(_ as String, servletRequest)
        1 * filterChain.doFilter(servletRequest, servletResponse)
    }

	def "validate request without entitlements token"() {
		given:
		EntitlementAuthFilter filter = new EntitlementAuthFilter();
		def servletRequest = Mock(HttpServletRequest.class);
		def servletResponse = Mock(HttpServletResponse.class)
		def filterChain = Mock(FilterChain.class)
		def response = (HttpServletResponse)servletResponse
		1 * servletRequest.getHeader(X_CAT_ENTITLEMENTS_HEADER) >> null
		servletRequest.getRequestURI() >> "recommendations/v1/recommendations"

		when:
		filter.doFilter(servletRequest, servletResponse, filterChain)

		then:
		1 * response.sendError(HttpStatus.UNAUTHORIZED.value(), NOT_AUTHORIZED_ENTITLEMENT_TOKEN_MISSING_MSG)
		0 * filter.doFilter(request, response)
	}

}
