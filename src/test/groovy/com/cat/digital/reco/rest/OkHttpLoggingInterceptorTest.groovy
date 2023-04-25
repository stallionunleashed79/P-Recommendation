package com.cat.digital.reco.rest

import com.cat.digital.reco.exceptions.DealerDetailException
import com.cat.digital.reco.utils.rest.OkHttpLoggingInterceptor
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import spock.lang.Shared
import spock.lang.Specification

class OkHttpLoggingInterceptorTest extends Specification {

    @Shared
    OkHttpLoggingInterceptor okHttpLoggingInterceptor
    Interceptor.Chain chain = Mock()

    def setup() {
        okHttpLoggingInterceptor = new OkHttpLoggingInterceptor(true, true)
    }

    def "test okhttp logging interceptor unhappy path"() {
        given:
        def e = new IOException()
        def request = new Request.Builder().url("http://url.com").build()
        chain.request() >> request
        chain.proceed(_ as Request) >> { throw e }

        when:
        okHttpLoggingInterceptor.intercept(chain)

        then:
        def eThrown = thrown(IOException)
        assert eThrown == e
    }

}


