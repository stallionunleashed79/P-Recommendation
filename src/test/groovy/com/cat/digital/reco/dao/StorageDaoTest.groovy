package com.cat.digital.reco.dao

import com.cat.digital.reco.dao.impl.StorageDaoImpl
import com.cat.digital.reco.domain.models.Attachment
import com.cat.digital.reco.utils.rest.OkResterImpl
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import spock.lang.Specification

import java.time.OffsetDateTime

class StorageDaoTest extends Specification {
    StorageDao storageDao

    OkResterImpl okRester = Mock()
    String url = "http://localhost:8080"
    String path = "/catDigital/storage/v1/files/metadata"

    String dealerCode = "TD030"

    String storageUrl = "${url}${path}?partyNumber=${dealerCode}&limit=1000"

    def requestBody = RequestBody.create(MediaType
            .parse("application/json"),
            "test"
    )

    Request expectedRequest = new Request.Builder().url(storageUrl).post(requestBody).build()

    String fileId = "8e84cf3d-b815-40d5-9750-1e4784fc5b75"
    String fileName = "report.xls"
    OffsetDateTime uploadTime = OffsetDateTime.parse("2016-01-31T22:59:59.999+00:00")

    String recommendationNumber = "REC-123-456-789"

    String bodyContent = "{" +
            "  \"files\": [" +
            "    {" +
            "      \"id\": \"${fileId}\"," +
            "      \"referenceId\": \"505508dd7c3149868ee5f5513398490a\"," +
            "      \"referenceType\": \"reco\"," +
            "      \"createdBy\": {" +
            "        \"catrecid\": \"PIP-11214098\"," +
            "        \"firstName\": \"Ned\"," +
            "        \"lastName\": \"Stark\"" +
            "      }," +
            "      \"fileName\": \"${fileName}\"," +
            "      \"contentType\": \"application/text\"," +
            "      \"status\": \"uploaded\"," +
            "      \"uploadTime\": \"${uploadTime}\"," +
            "      \"expirationTime\": \"2016-01-31T22:59:59.999+00:00\"," +
            "      \"origin\": \"Assets List Export\"" +
            "    }," +
            "    {" +
            "      \"id\": \"1234\"," +
            "      \"referenceId\": \"4567\"," +
            "      \"referenceType\": \"reco\"," +
            "      \"createdBy\": {" +
            "        \"catrecid\": \"PIP-11214098\"," +
            "        \"firstName\": \"Ned\"," +
            "        \"lastName\": \"Stark\"" +
            "      }," +
            "      \"fileName\": \"ignoreFile\"," +
            "      \"contentType\": \"application/text\"," +
            "      \"status\": \"awaitingUpload\"," +
            "      \"expirationTime\": \"2016-01-31T22:59:59.999+00:00\"," +
            "      \"origin\": \"Assets List Export\"" +
            "    }" +
            "  ]," +
            "  \"responseMetadata\": {" +
            "    \"nextCursor\": \"eyJvZmZzZXQiOjF9\"" +
            "  }" +
            "}"

    def response = new Response.Builder()
            .request(new Request.Builder().url("http://url.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("dummy message")
            .body(ResponseBody.create(MediaType.parse("application/json"), bodyContent))
            .build()

    def setup() {
        storageDao = new StorageDaoImpl(okRester, url, path)
    }

    def "when getting file meta data make a request to the storage endpoint with the correct url"() {
        when:
        storageDao.retrieveFileMetadata(recommendationNumber, dealerCode)

        then:
        1 * okRester.makeRequest({
            it.url() == expectedRequest.url()
        }) >> response
    }

    def "when getting file meta data return an array of file metadata on success"() {
        when:
        okRester.makeRequest(_ as Request) >> response

        def attachment = new Attachment()
        attachment.name = fileName
        attachment.fileId = fileId
        attachment.fileAttachedTime = uploadTime

        def result = storageDao.retrieveFileMetadata(recommendationNumber, dealerCode)

        then:
        result == [attachment]
    }

    def "when getting file meta data return an empty array return null"() {
        when:
        String bodyContent = "{" +
                "  \"files\": []," +
                "  \"responseMetadata\": {" +
                "    \"nextCursor\": \"\"" +
                "  }" +
                "}"

        def response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("dummy message")
                .body(ResponseBody.create(MediaType.parse("application/json"), bodyContent))
                .build()
        
        okRester.makeRequest(_ as Request) >> response

        def attachment = new Attachment()
        attachment.name = fileName
        attachment.fileId = fileId
        attachment.fileAttachedTime = uploadTime

        def result = storageDao.retrieveFileMetadata(recommendationNumber, dealerCode)

        then:
        result == null
    }

    def "when getting file meta data throws exception then return an empty array of attachments"() {
        when:
        def e = new IOException()
        okRester.makeRequest(_ as Request) >> { throw e }

        def result = storageDao.retrieveFileMetadata(recommendationNumber, dealerCode)

        then:
        result == null;
    }
}
