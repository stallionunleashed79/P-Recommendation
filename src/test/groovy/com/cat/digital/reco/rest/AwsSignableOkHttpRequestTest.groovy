package com.cat.digital.reco.rest

import com.cat.digital.reco.utils.rest.AwsSignableOkHttpRequest
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okio.BufferedSink
import spock.lang.Specification

class AwsSignableOkHttpRequestTest extends Specification {

    AwsSignableOkHttpRequest awsSignableOkHttpRequest

    def "Test headerArrayToMap Success"() {
        given:
        awsSignableOkHttpRequest = new AwsSignableOkHttpRequest(new Request.Builder().url("http://cat.com").header("a", "b").build())
        awsSignableOkHttpRequest.addHeader("a", "b")
        when:
        def result = awsSignableOkHttpRequest.generateRequest()
        then:
        result.headers().size() == 1
        result.header("a") == "b"
    }

    def "Test headerArrayToMap Success 2"() {
        given:
        awsSignableOkHttpRequest = new AwsSignableOkHttpRequest(new Request.Builder().url("http://cat.com").header("a", "b").build())
        awsSignableOkHttpRequest.addHeader("x", "y")
        when:
        Request result = awsSignableOkHttpRequest.generateRequest()
        then:
        result.headers().size() == 2
        result.header("a") == "b"
        result.header("x") == "y"
    }

    def "Test copyHeadersBack Success"() {
        given:
        awsSignableOkHttpRequest = new AwsSignableOkHttpRequest(new Request.Builder().url("http://cat.com/x?a=b").build())
        when:
        Request result = awsSignableOkHttpRequest.generateRequest()
        then:
        result.url().queryParameterNames().size() == 1
        result.url().queryParameterValues("a").size() == 1
        result.url().queryParameterValues("a").get(0) == "b"
    }

    def "Test copyHeadersBack Success 2"() {
        given:
        awsSignableOkHttpRequest = new AwsSignableOkHttpRequest(new Request.Builder().url("http://cat.com/x?a=b").build())
        awsSignableOkHttpRequest.addParameter("a", "y");
        when:
        Request result = awsSignableOkHttpRequest.generateRequest()
        then:
        result.url().queryParameterNames().size() == 1
        result.url().queryParameterValues("a").size() == 1
        result.url().queryParameterValues("a").get(0) == "b"
    }

    def "Test getContent Success"() {
        given:
        Request request = new Request.Builder().url("http://cat.com").header(
                "a", "b").post(RequestBody
                .create(MediaType
                        .parse("application/json"),
                        "test"
                )).build()

        awsSignableOkHttpRequest = new AwsSignableOkHttpRequest(request)

        when:
        def result = awsSignableOkHttpRequest.getContent()

        then:
        assert result != null
    }

    def "Test empty header Success"() {
        given:
        Request request = new Request.Builder().url("http://cat.com").post(RequestBody
                .create(MediaType
                        .parse("application/json"),
                        "test"
                )).build()

        awsSignableOkHttpRequest = new AwsSignableOkHttpRequest(request)

        when:
        def result = awsSignableOkHttpRequest.generateRequest()

        then:
        result.headers().size() == 0
    }

    def "Test getContent throws IOException"() {
        given:
        def e = new IOException()
        RequestBody requestBody = Mock()
        requestBody.writeTo(_ as BufferedSink) >> { throw e }
        Request request = new Request.Builder().url("http://cat.com").header(
                "a", "b").post(requestBody).build()

        awsSignableOkHttpRequest = new AwsSignableOkHttpRequest(request)

        when:
        awsSignableOkHttpRequest.getContent()

        then:
        thrown(IOException)
    }
}
