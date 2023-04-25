package com.cat.digital.reco.entitlement

import com.cat.digital.reco.dao.DealerDetailsDao
import com.cat.digital.reco.domain.models.Authorization
import com.cat.digital.reco.exceptions.AuthorizationException
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

import static com.cat.digital.reco.common.Constants.*

@WebMvcTest(EntitlementUtils.class)
class EntitlementUtilsTest extends Specification {

    EntitlementUtils entitlementUtils

    @SpringBean
    DealerDetailsDao dealerDetailsDao = Mock()

    def setup() {
        entitlementUtils = new EntitlementUtils()
    }

    def "Test EntitlementToken is not base64 encoded"() {
        given:
        HttpServletRequest request = Mock()
        String entitlementToken = "aadkfjdkfjl23dksfjkad"
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        entitlementUtils.getEntitlements(entitlementToken, request)
        then:
        AuthorizationException ex = thrown()

        ex.message.contains(NOT_AUTHORIZED_ENTITLEMENT_TOKEN_INVALID_MSG)
    }

    def "Test EntitlementToken is missing"() {
        given:
        HttpServletRequest request = Mock()
        String entitlementToken = null;
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        AuthorizationException ex = thrown()
        ex.message.contains(NOT_AUTHORIZED_ENTITLEMENT_TOKEN_MISSING_MSG)
    }

    def "Test EntitlementToken is not valid"() {
        given:
        HttpServletRequest request = Mock()
        String entitlementToken = "dkfjdkfjl23dksfjkad"
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        AuthorizationException ex = thrown()
        ex.message.contains(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_RECOMMENDATIONS_MSG)
    }

    def "Test EntitlementToken not having recommendation section in the token"() {
        given:
        HttpServletRequest request = Mock()
        String entitlementToken = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0yRDI2QURDQSIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJDVkExIjogewogICAgICAgICJERUxFVEUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJQT1NUIjogewogICAgICAgICAgImFsbG93ZWRGaWVsZHMiOiBbCiAgICAgICAgICAgICIqIgogICAgICAgICAgXSwKICAgICAgICAgICJmaWx0ZXJDb25kaXRpb25zIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgInBhcnR5TnVtYmVycyI6IFsKICAgICAgICAgICAgICAgICJURDAwIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgIlBVVCI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQ0FUIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgIkdFVCI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiVERPTyIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9CiAgICAgIH0KICAgIH0KICB9LAogICJhbGciOiAiSFMyNTYiCn0="
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        AuthorizationException ex = thrown()
        ex.message.contains(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_RECOMMENDATIONS_MSG)
    }

    def "Test EntitlementToken not having catrecId"(){
        given:
        String entitlementToken  = "ewogICJhdXRob3JpemF0aW9uIjogewoKICAgICJwZXJtaXNzaW9ucyI6IHsKICAgICAgInJlY29tbWVuZGF0aW9ucyI6IHsKICAgICAgICAidmlldyI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQ0FUIiwgIlQwMzAiCiAgICAgICAgICAgICAgXSwKICAgICAgICAgICAgICAicmVnaW9ucyI6IFsKICAgICAgICAgICAgICAgICJBRFNELU4iCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfSwKICAgICAgICAiY3JlYXRlIjogewogICAgICAgICAgImFsbG93ZWRGaWVsZHMiOiBbCiAgICAgICAgICAgICIqIgogICAgICAgICAgXSwKICAgICAgICAgICJmaWx0ZXJDb25kaXRpb25zIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgInBhcnR5TnVtYmVycyI6IFsKICAgICAgICAgICAgICAgICJUMDMwIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgInVwZGF0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQ0FUIiwgIlQwMzAiCiAgICAgICAgICAgICAgXSwKICAgICAgICAgICAgICAicmVnaW9ucyI6IFsKICAgICAgICAgICAgICAgICJBRFNELU4iCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfSwKICAgICAgICAiZGVsZXRlIjogewogICAgICAgICAgImFsbG93ZWRGaWVsZHMiOiBbCiAgICAgICAgICAgICIqIgogICAgICAgICAgXSwKICAgICAgICAgICJmaWx0ZXJDb25kaXRpb25zIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgInBhcnR5TnVtYmVycyI6IFsKICAgICAgICAgICAgICAgICJCMDMwIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0KICAgICAgfQogICAgfQogIH0KfQ=="
        HttpServletRequest request = Mock()
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        AuthorizationException ex = thrown()
        ex.message.contains(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_CATRECID_MSG)
    }

    def "Test Entitlement when loggedin user is a SuperUser or CATALL"(){
        given:
        String entitlementToken  = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0zMUU5QjZERiIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJyZWNvbW1lbmRhdGlvbnMiOiB7CiAgICAgICAgInZpZXciOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVEFMTCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJjcmVhdGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVEFMTCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJ1cGRhdGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVCIsICJUMDMwIgogICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgInJlZ2lvbnMiOiBbCiAgICAgICAgICAgICAgICAiQURTRC1OIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgImRlbGV0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQjAzMCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9CiAgICAgIH0KICAgIH0KICB9Cn0="
        HttpServletRequest request = Mock()
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        def result = entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        assert result != null
    }

    def "Test Entitlement when loggedin user is a dealer user"(){
        given:
        String entitlementToken  = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0wMDAxQjQ4RSIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJyZWNvbW1lbmRhdGlvbnMiOiB7CiAgICAgICAgInZpZXciOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIlQwMzAiCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfSwKICAgICAgICAiY3JlYXRlIjogewogICAgICAgICAgImFsbG93ZWRGaWVsZHMiOiBbCiAgICAgICAgICAgICIqIgogICAgICAgICAgXSwKICAgICAgICAgICJmaWx0ZXJDb25kaXRpb25zIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgICAiVDAzMCIKICAgICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgInVwZGF0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiVDAzMCIsICJDQVQiCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfSwKICAgICAgICAiZGVsZXRlIjogewogICAgICAgICAgImFsbG93ZWRGaWVsZHMiOiBbCiAgICAgICAgICAgICIqIgogICAgICAgICAgXSwKICAgICAgICAgICJmaWx0ZXJDb25kaXRpb25zIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgInBhcnR5TnVtYmVycyI6IFsKICAgICAgICAgICAgICAgIlQwMzAiCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfQogICAgICB9CiAgICB9CiAgfQp9"
        HttpServletRequest request = Mock()
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        def result = entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        assert result != null
    }

    def "Test Entitlement when loggedin user is a CAT user"(){
        given:
        String entitlementToken  = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0wMDAxQjQ4RSIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJyZWNvbW1lbmRhdGlvbnMiOiB7CiAgICAgICAgInZpZXciOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJjcmVhdGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAgICJDQVQiCiAgICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJ1cGRhdGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJkZWxldGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAiQ0FUIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0KICAgICAgfQogICAgfQogIH0KfQ=="
        HttpServletRequest request = Mock()
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        def result = entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        assert result != null
        assert result.authorizedDealers.contains(CAT)
    }

    def "Test throws exception when there are no party numbers for a loggedin user"() {
        given:
        HttpServletRequest request = Mock()
        String entitlementToken = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0zMUU5QjZERiIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJyZWNvbW1lbmRhdGlvbnMiOiB7CiAgICAgICAgInZpZXciOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVCIsICJCMDMwIgogICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgInJlZ2lvbnMiOiBbCiAgICAgICAgICAgICAgICAiQURTRC1OIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgImNyZWF0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgIAogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgInVwZGF0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQ0FUIgogICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgInJlZ2lvbnMiOiBbCiAgICAgICAgICAgICAgICAiQURTRC1OIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgImRlbGV0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQjAzMCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9CiAgICAgIH0KICAgIH0KICB9Cn0="
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/abc"

        when:
        entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        0 * dealerDetailsDao.getDealerCodes(_ as List<String>)

        then:
        AuthorizationException ex = thrown()
        ex.message.contains(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_PARTY_MSG)
    }

    // Tests isAuthorized()
    def "Test is authorized authorized dealers does not contain dealer code"() {
        given:
        def authorization = new Authorization(catRecId: "TestRecID", authorizedDealers: ["NOTCAT", "TEST"])
        def dealerCode = "YYYY"

        when:
        entitlementUtils.validatePartyNumbers(authorization, dealerCode)

        then:
        AuthorizationException ex = thrown()
        ex.message.contains(NOT_HEADER_FORBIDDEN_MSG)
    }

    def "Test is authorized for CAT User"() {
        given:
        def authorization = new Authorization(catRecId: "TestRecID", authorizedDealers: [CAT])
        def dealerCode = "YYYY"

        when:
        entitlementUtils.validatePartyNumbers(authorization, dealerCode)

        then:
        noExceptionThrown()
    }

    def "Test is authorized happy flow multiple partyNumbers"() {
        given:
        def authorization = new Authorization(catRecId: "TestRecID", authorizedDealers: ["NOTCAT", "TEST", "YYYY"])
        def dealerCode = "YYYY"

        when:
        def result = entitlementUtils.validatePartyNumbers(authorization, dealerCode)

        then:
        result == null
    }

    def "Test is authorized forbidden"() {
        given:
        def authorization = new Authorization(catRecId: "TestRecID", authorizedDealers: ["TEST", "ABC"])
        def dealerCode = "TEST-CAT"

        when:
        entitlementUtils.validatePartyNumbers(authorization, dealerCode)

        then:
        thrown(AuthorizationException)
    }

    def "Test is view recommendation forbidden for a dealer"() {
        given:
        def authorization = new Authorization(catRecId: "TestRecID", authorizedDealers: ["TEST", "ABC"], isDealer: true)

        when:
        entitlementUtils.validateViewAction(authorization, false)

        then:
        thrown(AuthorizationException)
    }

    def "Test is update recommendation forbidden for a dealer"() {
        given:
        def authorization = new Authorization(catRecId: "TestRecID", authorizedDealers: ["TEST", "ABC"], isDealer: true)

        when:
        entitlementUtils.validateUpdateAction(authorization, false)

        then:
        thrown(AuthorizationException)
    }

    def "Test is update recommendation forbidden for a CAT or CATALL user"() {
        given:
        def authorization = new Authorization(catRecId: "TestRecID", authorizedDealers: ["TEST", "ABC"], isDealer: false)

        when:
        entitlementUtils.validateUpdateAction(authorization, true)

        then:
        thrown(AuthorizationException)
    }

    def "Test throws exception there is no view role for a dealer user on a GET recommendation details API call"() {
        given:
        HttpServletRequest request = Mock()
        String entitlementToken = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0wMDAxQjQ4RSIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJyZWNvbW1lbmRhdGlvbnMiOiB7CiAgICAgICAgImNyZWF0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgInBhcnR5TnVtYmVycyI6IFsKICAgICAgICAgICAgICAgICAgIlQwMzAiCiAgICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJ1cGRhdGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIlQwMzAiCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfSwKICAgICAgICAiZGVsZXRlIjogewogICAgICAgICAgImFsbG93ZWRGaWVsZHMiOiBbCiAgICAgICAgICAgICIqIgogICAgICAgICAgXSwKICAgICAgICAgICJmaWx0ZXJDb25kaXRpb25zIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgInBhcnR5TnVtYmVycyI6IFsKICAgICAgICAgICAgICAgIlQwMzAiCiAgICAgICAgICAgICAgXQogICAgICAgICAgICB9CiAgICAgICAgICBdCiAgICAgICAgfQogICAgICB9CiAgICB9CiAgfQp9"
        request.getMethod() >> "GET"
        request.getRequestURI() >> "/abc"

        when:
        entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        AuthorizationException ex = thrown()
        ex.message.contains(NOT_AUTHORIZED_ENTITLEMENT_HDR_INVALID_ROLE_MSG)
    }

    def "Test Entitlement role is mapped to \"view\" when calling the search recommendation endpoint"(){
        given:
        String entitlementToken  = "ewogICJhdXRob3JpemF0aW9uIjogewogICAgImNhdHJlY2lkIjogIlFQUy0zMUU5QjZERiIsCiAgICAicGVybWlzc2lvbnMiOiB7CiAgICAgICJyZWNvbW1lbmRhdGlvbnMiOiB7CiAgICAgICAgInZpZXciOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVEFMTCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJjcmVhdGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVEFMTCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9LAogICAgICAgICJ1cGRhdGUiOiB7CiAgICAgICAgICAiYWxsb3dlZEZpZWxkcyI6IFsKICAgICAgICAgICAgIioiCiAgICAgICAgICBdLAogICAgICAgICAgImZpbHRlckNvbmRpdGlvbnMiOiBbCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAicGFydHlOdW1iZXJzIjogWwogICAgICAgICAgICAgICAgIkNBVCIsICJUMDMwIgogICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgInJlZ2lvbnMiOiBbCiAgICAgICAgICAgICAgICAiQURTRC1OIgogICAgICAgICAgICAgIF0KICAgICAgICAgICAgfQogICAgICAgICAgXQogICAgICAgIH0sCiAgICAgICAgImRlbGV0ZSI6IHsKICAgICAgICAgICJhbGxvd2VkRmllbGRzIjogWwogICAgICAgICAgICAiKiIKICAgICAgICAgIF0sCiAgICAgICAgICAiZmlsdGVyQ29uZGl0aW9ucyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICJwYXJ0eU51bWJlcnMiOiBbCiAgICAgICAgICAgICAgICAiQjAzMCIKICAgICAgICAgICAgICBdCiAgICAgICAgICAgIH0KICAgICAgICAgIF0KICAgICAgICB9CiAgICAgIH0KICAgIH0KICB9Cn0="
        HttpServletRequest request = Mock()
        request.getMethod() >> "POST"
        request.getRequestURI() >> "/search"

        when:
        def result = entitlementUtils.getEntitlements(entitlementToken, request)

        then:
        assert result != null
    }
}
