package com.cat.digital.reco.dao

import com.cat.digital.reco.domain.models.UserManagementData
import com.cat.digital.reco.exceptions.EntityNotFoundException
import com.cat.digital.reco.utils.rest.OkResterImpl
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import spock.lang.Specification

@WebMvcTest(UserManagementDao.class)
class UserManagementDaoTest extends Specification {

    @SpringBean
    OkResterImpl okRester = Mock()

    @SpringBean
    ObjectMapper mapper = Mock()

    @Autowired
    UserManagementDao userManagementDao

    Response response
    def body = ""

    def setup() {
        body = "{catrecid: \"cat rec id\", firstName: \"dummy first name\", lastName: \"dummy last name\", username: \"cws_id\"}"
        response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("dummy message")
                .body(okhttp3.ResponseBody.create(
                        MediaType.parse("application/json"), body))
                .build()

    }

    def "success call from user management API"() {
        given:
        def catRecId = "cat rec id"
        def userData = new UserManagementData(firstName: "dummy first name", lastName: "dummy last name", username: "cws_id")

        when:
        def result = userManagementDao.getUserData(catRecId)

        then:
        result != null
        1 * okRester.makeRequest(_ as Request) >> response
        1 * mapper.readValue(_ as String, _ as Class) >> userData
    }

    def "error call from user management API"() {
        given:
        def catRecId = "cat rec id"
        response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("dummy message")
                .body(okhttp3.ResponseBody.create(
                        MediaType.parse("application/json"), body))
                .build()

        when:
        userManagementDao.getUserData(catRecId)

        then:
        thrown EntityNotFoundException
        1 * okRester.makeRequest(_ as Request) >> response
        0 * mapper.readValue(_ as String, _ as Class)
    }
}
