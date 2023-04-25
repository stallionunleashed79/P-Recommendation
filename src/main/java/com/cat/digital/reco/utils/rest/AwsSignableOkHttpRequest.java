/*
 * Copyright (c) 2019 Caterpillar Inc. All Rights Reserved.
 *
 * This work contains Caterpillar Inc.'s unpublished
 * proprietary information which may constitute a trade secret
 * and/or be confidential. This work may be used only for the
 * purposes for which it was provided, and may not be copied
 * or disclosed to others. Copyright notice is precautionary
 * only, and does not imply publication.
 */

package com.cat.digital.reco.utils.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.amazonaws.ReadLimitInfo;
import com.amazonaws.SignableRequest;
import com.amazonaws.http.HttpMethodName;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okio.Buffer;


/**
 * signs an OkHttp request in AWS.
 */
@Log4j2
@Data
public class AwsSignableOkHttpRequest implements SignableRequest {
  private final Request request;
  private final Map<String, String> headers;
  private final Map<String, List<String>> parameters;
  private URI endpoint;
  private int readLimit = 131073;

  /**
   * Returns aws signable request.
   *
   * @param request request to be signed.
   */
  public AwsSignableOkHttpRequest(Request request) {
    this.request = request;
    HttpUrl url = request.url();
    try {
      endpoint = new URI(String.format("%s://%s", url.scheme(), url.host()));
    } catch (Exception e) {
      log.error("Error creating uri in request signature schema {}, host {}", url.scheme(), url.host());
    }
    parameters = urlToMapParams(url);
    headers = headerArrayToMap(request.headers());
  }

  @Override
  public void addHeader(String key, String value) {
    headers.put(key, value);
  }

  @Override
  public void addParameter(String key, String value) {
    // there should be no reason for the signer to call this
  }

  @Override
  public String getResourcePath() {
    return request.url().url().getPath();
  }

  @Override
  public HttpMethodName getHttpMethod() {
    return HttpMethodName.fromValue(request.method());
  }

  @Override
  public int getTimeOffset() {
    return 0;
  }

  @SneakyThrows
  @Override
  public InputStream getContent() {
    if (request.body() != null) {
      final Request copy = request.newBuilder().build();
      final Buffer buffer = new Buffer();
      copy.body().writeTo(buffer);
      return new ByteArrayInputStream(buffer.readByteArray());
    }
    return null;
  }

  @Override
  public void setContent(InputStream inputStream) {
    // there should be no reason for the signer to call this
  }

  @Override
  public InputStream getContentUnwrapped() {
    return getContent();
  }

  public void setReadLimit(int readLimit) {
    this.readLimit = readLimit;
  }

  @Override
  public ReadLimitInfo getReadLimitInfo() {
    return () -> readLimit;
  }

  @Override
  public Object getOriginalRequestObject() {
    return request;
  }

  public Request generateRequest() {
    Request.Builder builder = request.newBuilder();
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      builder.header(entry.getKey(), entry.getValue());
    }
    return builder.build();
  }

  /**
   * Url to map.
   *
   * @param url the url for the request
   * @return a multimap of HTTP query params
   */
  private Map<String, List<String>> urlToMapParams(HttpUrl url) {
    Map<String, List<String>> parameterMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    for (String name : url.queryParameterNames()) {
      parameterMap.put(name, url.queryParameterValues(name));
    }
    return parameterMap;
  }

  /**
   * Headers to map.
   *
   * @param headers modeled Header objects
   * @return a Map of header entries
   */
  private Map<String, String> headerArrayToMap(final Headers headers) {
    Map<String, String> headersMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    for (Map.Entry<String, List<String>> entry : headers.toMultimap().entrySet()) {
      if ((!isZeroContentLenth(entry.getKey(), entry.getValue()))
          && (!("host".equalsIgnoreCase(entry.getKey())))) {
        if (!entry.getValue().isEmpty()) {
          headersMap.put(entry.getKey(), entry.getValue().get(0));
        } else {
          headersMap.put(entry.getKey(), "");
        }
      }
    }
    return headersMap;
  }

  private boolean isZeroContentLenth(String key, List<String> value) {
    if (value.size() == 1) {
      return ("content-length".equalsIgnoreCase(key) && ("0".equals(value.get(0))));
    } else {
      return false;
    }
  }
}