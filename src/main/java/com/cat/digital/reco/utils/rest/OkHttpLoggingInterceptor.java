package com.cat.digital.reco.utils.rest;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.HttpHeaders;

@Log4j2
public class OkHttpLoggingInterceptor implements Interceptor {

  private static final long MAX_RESPONSE_BYTES = 5120L;


  private final boolean logRequestEntity;
  private final boolean logResponseEntity;

  /**
   * Construct.
   */
  public OkHttpLoggingInterceptor(boolean logRequestEntity, boolean logResponseEntity) {
    this.logRequestEntity = logRequestEntity;
    this.logResponseEntity = logResponseEntity;
  }

  /*
   * (non-Javadoc)
   *
   * @see okhttp3.Interceptor#intercept(okhttp3.Interceptor.Chain)
   */
  @Override
  public Response intercept(final Chain chain) throws IOException {
    StopWatch sw = StopWatch.createStarted();
    Request request = chain.request();
    Response response = null;
    try {
      response = chain.proceed(request);
    } catch (Exception e) {
      log.error("Error occurred while getting intercepted error. [{}].", e.getLocalizedMessage(), e);
      throw e;
    }

    if (response.isSuccessful()) { //lets not log successes!
      return response;
    }

    StringBuilder sb = new StringBuilder("HTTP:");
    sb.append(response.code()).append("(").append(response.message()).append(")");
    sb.append(" - Verb:").append(request.method());
    sb.append(" - Time:[").append(sw.getTime()).append("ms]");
    sb.append(" - Path:").append(request.url());
    sb.append(" - Content-type:")
        .append(StringUtils.defaultIfBlank(request.header(HttpHeaders.CONTENT_TYPE), "None"));
    sb.append(" - Accept:").append(StringUtils.defaultIfBlank(request.header("Accept"), "None"));
    if (logRequestEntity && request.body() != null) {
      sb.append(" - RequestBody:").append(request.body().toString());
    }
    if (logResponseEntity) {
      String text = response.peekBody(MAX_RESPONSE_BYTES).string();
      if (StringUtils.isNotBlank(text)) {
        sb.append(" - ResponseBody:").append(text.replaceAll("\\r\\n|\\r|\\n", " "));
      }
    }
    log.debug(sb.toString());

    return response;
  }
}
