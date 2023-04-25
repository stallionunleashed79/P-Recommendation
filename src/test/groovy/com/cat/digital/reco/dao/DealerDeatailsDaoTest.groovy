package com.cat.digital.reco.dao

import com.cat.digital.reco.common.CustomResponseCodes
import com.cat.digital.reco.dao.impl.DealerDetailsDaoImpl
import com.cat.digital.reco.exceptions.DealerDetailException
import static com.cat.digital.reco.common.Constants.*;
import com.cat.digital.reco.utils.rest.OkResterImpl
import okhttp3.Request
import okhttp3.Response
import okhttp3.Protocol
import okhttp3.MediaType
import okhttp3.ResponseBody
import spock.lang.Specification

class DealerDeatailsDaoTest extends Specification {

  DealerDetailsDaoImpl dealerDetailsDao;
  OkResterImpl okRester = Mock()
  String url = 'http://localhost:8080'
  String bodyContent = "{\"dealers\":[\"TD00\",\"TD67\",\"B010\"]}"
  String dealerPath = '/dealerDetails/v1/dealers/propertySearch'

  def setup() {
    dealerDetailsDao =  new DealerDetailsDaoImpl(url, okRester, dealerPath)
  }

  def "test get request"() {
    given:
    def regionCodes = ["test"]

    when:
    def result = dealerDetailsDao.getResponse(regionCodes)

    then:
    result == null
  }

  def "test url builder"() {
    given:
    def regionCodes = ["test"]

    when:
    def result = dealerDetailsDao.addQueryParams(regionCodes)

    then:
    result == 'http://localhost:8080/dealerDetails/v1/dealers/propertySearch?regionCodes=test&property=dealerCode'
  }

  def "test get Dealer codes"() {
    given:
    def response = new Response.Builder()
            .request(new Request.Builder().url("http://url.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(400)
            .message("dummy message")
            .body(ResponseBody.create(
                    MediaType.parse("application/json"), "")).build()
    okRester.makeRequest(_ as Request) >> response

    when:
    dealerDetailsDao.getDealerCodes(["TD00"])

    then:
    def e = thrown(DealerDetailException)
    e.code == CustomResponseCodes.NOT_AUTHORIZED_REQUEST_EXCEPTION
    e.message == NOT_AUTHORIZED_DEALER_REQUEST_EXCEPTION_MSG
  }

  def "test get Dealer codes happy path"() {
    given:
    def response = new Response.Builder()
            .request(new Request.Builder().url("http://url.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("dummy message")
            .body(ResponseBody.create(
                    MediaType.parse("application/json"), bodyContent)).build()
    1 * okRester.makeRequest(_ as Request) >> response

    when:
    def result = dealerDetailsDao.getDealerCodes(["TD00"])

    then:

    assert result != null
  }
}
