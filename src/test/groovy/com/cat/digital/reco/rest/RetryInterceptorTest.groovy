package com.cat.digital.reco.rest

import com.cat.digital.reco.utils.rest.RetryInterceptor
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import spock.lang.Specification

class RetryInterceptorTest extends Specification {

    RetryInterceptor retryInterceptor
    Interceptor.Chain chain = Mock()

    def setup() {
        retryInterceptor = new RetryInterceptor(3)
    }

    def "unhappy path test retry failure scenario"() {
        given:
        def request = new Request.Builder().url("http://url.com").build()
        def response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("dummy message")
                .body(ResponseBody.create(
                        MediaType.parse("application/json"), "")).build()
        chain.request() >> request

        when:
        def result = retryInterceptor.intercept(chain)

        then:
        3 * chain.proceed(request) >> response
        assert result == response
    }

    def "happy path test success scenario"() {
        given:
        def request = new Request.Builder().url("http://url.com").build()
        def response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("dummy message")
                .body(ResponseBody.create(
                        MediaType.parse("application/json"), "")).build()
        chain.request() >> request

        when:
        def result = retryInterceptor.intercept(chain)

        then:
        1 * chain.proceed(request) >> response
        assert result == response
    }
}
