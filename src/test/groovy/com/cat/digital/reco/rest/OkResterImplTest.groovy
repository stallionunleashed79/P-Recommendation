package com.cat.digital.reco.rest


import com.cat.digital.reco.utils.rest.OkResterImpl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import spock.lang.Shared
import spock.lang.Specification

class OkResterImplTest extends Specification {

    OkResterImpl okRester;

    private int connectionTimeOut = 5

    private int connectionReadTimeOut= 5

    private boolean isLogRequestEntity = true

    private boolean isLogResponseEntity = true

    private int maxRetry = 3

    @Shared
    MockWebServer server

    def setup() {
        server = new MockWebServer()
        okRester = new OkResterImpl(connectionTimeOut, connectionReadTimeOut, maxRetry, isLogRequestEntity, isLogResponseEntity, false)
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken("accessToken", null, null))
    }

    def "test make request for external api call"() {
        given:
        okRester = new OkResterImpl(connectionTimeOut, connectionReadTimeOut, maxRetry, isLogRequestEntity, isLogResponseEntity, false)
        server.enqueue(
                new MockResponse()
                        .setBody("Mock server response")
                        .setHeader('Content-Type', 'text/xml;charset=UTF-8')
        )
        server.start()

        def url = server.url("/");
        def request = new Request.Builder()
                .get()
                .url(url)
                .build();
        when:
        def result = okRester.makeRequest(request);

        then:
        assert result != null
        assert result.isSuccessful()
    }

    def "test make request for external api call with proxy enable"() {
        given:
        def proxy = true

        when:
        okRester = new OkResterImpl(connectionTimeOut, connectionReadTimeOut, maxRetry, isLogRequestEntity, isLogResponseEntity, proxy)

        then:
        assert okRester != null
    }

    def "test make request for internal api call with proxy enable"() {
        given:
        def dealerRegion = 'us-east-2'
        def proxy = true

        when:
        def result = new OkResterImpl(connectionTimeOut, connectionReadTimeOut, maxRetry, isLogRequestEntity, isLogResponseEntity, dealerRegion, proxy);

        then:
        assert result != null
    }

    def "test make request for internal api call without proxy"() {
        given:
        def dealerRegion = 'us-east-2'
        def proxy = false

        when:
        def result = new OkResterImpl(connectionTimeOut, connectionReadTimeOut, maxRetry, isLogRequestEntity, isLogResponseEntity, dealerRegion, proxy);

        then:
        assert result != null
    }

    def cleanup() {
        server.shutdown()
    }

}
