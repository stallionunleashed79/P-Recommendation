package com.cat.digital.reco.rest

import com.cat.digital.reco.utils.rest.OkHttpLoggingInterceptor
import com.cat.digital.reco.utils.rest.RetryInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import spock.lang.Shared
import spock.lang.Specification

class HttpLoggingInterceptorTest extends Specification {

    @Shared
    MockWebServer server

    def setup() {
        server = new MockWebServer()
    }

    def "test logging interceptor"() {
        given:

         server.enqueue(
         new MockResponse()
         .setBody("Mock server response")
         .setHeader('Content-Type', 'text/xml;charset=UTF-8')
         .setResponseCode(500))
         server.start()

         def url = server.url("/my-resource-path");

        def client = new OkHttpClient.Builder()
                .addInterceptor(new OkHttpLoggingInterceptor(true, true))
                //.addInterceptor(new RetryInterceptor(3))
                .build();
        def request = new Request.Builder()
                .get()
                .url(url)
                .build();

        when:
        def response = client.newCall(request).execute();

        then:
        assert response != null
    }

    def cleanup() {
        server.shutdown()
    }
}
