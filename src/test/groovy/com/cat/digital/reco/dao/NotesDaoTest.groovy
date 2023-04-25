package com.cat.digital.reco.dao

import com.cat.digital.reco.dao.impl.NotesDaoImpl
import com.cat.digital.reco.domain.models.RecommendationLink
import com.cat.digital.reco.utils.rest.OkResterImpl
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.Protocol
import spock.lang.Specification

import java.time.OffsetDateTime

class NotesDaoTest extends Specification {
    NotesDao notesDao

    OkResterImpl okRester = Mock()
    def url = "http://localhost:8080"
    def path = "/catDigital/notes/v1/notes"

    def recommendationNumber = "referenceId"
    def maxNotesPerLevel = "1000"
    def maxDepth = "5"

    def notesUrl = "${url}${path}?referenceId=${recommendationNumber}&maxNotesPerLevel=${maxNotesPerLevel}&maxDepth=${maxDepth}"

    Request expectedRequest = new Request.Builder().url(notesUrl).get().build()

    String createdDate1 = "2016-02-01T22:59:59.999+00:00"
    OffsetDateTime dateLinkAttached1 = OffsetDateTime.parse(createdDate1)
    String createdDate2 = "2016-02-01T22:59:59.999+00:00"
    OffsetDateTime dateLinkAttached2 = OffsetDateTime.parse(createdDate2)
    String content1 = "https://example.com"
    String content2 = "https://cat.com"

    String bodyContent = "{" +
            "  \"notes\": [" +
            "    {" +
            "      \"id\": \"51cd758f-55a3-40ff-a3c2-fff4c371ad99\"," +
            "      \"referenceId\": \"${recommendationNumber}\"," +
            "      \"referenceType\": \"instance\"," +
            "      \"status\": \"active\"," +
            "      \"content\": \"${content1}\"," +
            "      \"children\": []," +
            "      \"additionalTotalNotesCount\": 0," +
            "      \"additionalActiveNotesCount\": 0," +
            "      \"createTime\": \"${createdDate1}\"," +
            "      \"createdBy\": {" +
            "        \"catrecid\": \"PIP-11214098\"," +
            "        \"firstName\": \"FirstName\"," +
            "        \"lastName\": \"LastName\"" +
            "      }" +
            "    }," +
            "    {" +
            "        \"id\": \"953ec380-0538-487e-b65d-378f8bf535e9\"," +
            "        \"referenceId\": \"${recommendationNumber}\"," +
            "        \"referenceType\": \"instance\"," +
            "        \"status\": \"active\"," +
            "        \"content\": \"${content2}\"," +
            "        \"children\": [ ]," +
            "        \"additionalTotalNotesCount\": 0," +
            "        \"additionalActiveNotesCount\": 0," +
            "        \"createTime\": \"${createdDate2}\"," +
            "        \"createdBy\": {}" +
            "    }" +
            "  ]," +
            "  \"additionalTotalNotesCount\": 3," +
            "  \"additionalActiveNotesCount\": 3" +
            "}"

    def response = new Response.Builder()
            .request(expectedRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("Success")
            .body(ResponseBody.create(MediaType.parse("application/json"), bodyContent))
            .build()

    def setup() {
        notesDao = new NotesDaoImpl(okRester, url, path, maxNotesPerLevel, maxDepth)
    }

    def "when getting links make a request to the notes endpoint with the correct url"() {
        when:
            notesDao.getLinks(recommendationNumber)

        then:
            1 * okRester.makeRequest({
                it.url() == expectedRequest.url()
            }) >> response
    }

    def "when getting links returns a list of recommendation links on success"() {
        given:
            okRester.makeRequest(_ as Request) >> response

            def link1 = new RecommendationLink()
            link1.noteId = "51cd758f-55a3-40ff-a3c2-fff4c371ad99"
            link1.url = content1
            link1.attachedTime = dateLinkAttached1

            def link2 = new RecommendationLink()
            link2.noteId = "953ec380-0538-487e-b65d-378f8bf535e9"
            link2.url = content2
            link2.attachedTime = dateLinkAttached2

        when:
            def result = notesDao.getLinks()

        then:
            result == [link1, link2]
    }

    def "when getting links returns null when no files returned on success"() {
        given:
            String bodyContent = "{" +
                "  \"notes\": []," +
                "  \"additionalTotalNotesCount\": 0," +
                "  \"additionalActiveNotesCount\": 0" +
                "}"

            def response = new Response.Builder()
                .request(expectedRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("Success")
                .body(ResponseBody.create(MediaType.parse("application/json"), bodyContent))
                .build()

            okRester.makeRequest(_ as Request) >> response

        when:
            def result = notesDao.getLinks()

        then:
            result == null
    }

    def "when getting file meta data throws exception then return an empty array of links"() {
        when:
        def e = new IOException()
        okRester.makeRequest(_ as Request) >> { throw e }

        def result = notesDao.getLinks(recommendationNumber)

        then:
        result == null;
    }
}
