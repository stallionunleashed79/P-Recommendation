package com.cat.digital.reco.rest

import com.amazonaws.auth.AWS4Signer
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.cat.digital.reco.common.CustomResponseCodes
import com.cat.digital.reco.exceptions.DealerDetailException
import com.cat.digital.reco.utils.rest.AWSRequestSigningOkHttpInterceptor
import com.cat.digital.reco.utils.rest.AwsSignableOkHttpRequest
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.springframework.web.bind.annotation.RequestPart
import spock.lang.Specification

import static com.cat.digital.reco.common.Constants.NOT_AUTHORIZED_DEALER_REQUEST_EXCEPTION_MSG

class AWSRequestSigningOkHttpInterceptorTest extends Specification {

    Interceptor.Chain chain = Mock()
    AWS4Signer signer = Mock()
    AWSCredentialsProvider credentialsProvider = Mock()
    AWSCredentials awsCredentials = Mock()
    AWSRequestSigningOkHttpInterceptor awsRequestSigningOkHttpInterceptor
    def accessKeyId = "accessKeyId"
    def accessSecretKey = "accessSecretKey"

    def setup() {
        awsCredentials.getAWSAccessKeyId() >> accessKeyId
        awsCredentials.getAWSSecretKey() >> accessSecretKey
        credentialsProvider = new AWSCredentialsProvider() {
            @Override
            AWSCredentials getCredentials() {
                return awsCredentials
            }

            @Override
            void refresh() {

            }
        }
        awsRequestSigningOkHttpInterceptor = new AWSRequestSigningOkHttpInterceptor(
                signer, credentialsProvider)
    }

    def "test happy path intercept call"() {
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
        def result = awsRequestSigningOkHttpInterceptor.intercept(chain)

        then:
        1 * signer.sign(_ as AwsSignableOkHttpRequest, _ as AWSCredentials) >> null
        1 * chain.proceed(_ as Request) >> response
        assert result != null
    }
}
