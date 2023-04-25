package com.cat.digital.reco.utils.rest;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.internal.http.RealResponseBody;

@Log4j2
public class RetryInterceptor implements Interceptor  {

  private final int maxRetry;

  /**
   * Construct.
   */
  public RetryInterceptor(int retry) {
    this.maxRetry = retry;
  }

  @Override
  public Response intercept(final Chain chain) throws IOException {

    Request request = chain.request();

    // try the request
    var isSuccess = false;
    var tryCount = 0;
    Response response = new Response.Builder().body(new RealResponseBody("application/json", 0, null)).request(request).protocol(Protocol.get("http/1.0")).code(200).message("").build();

    do {
      response.close();
      log.debug("attempt # {} of {} to reach the URL {}", ++ tryCount, maxRetry, request.url());
      response = chain.proceed(request);
      isSuccess = response.isSuccessful();
    } while (!isSuccess && tryCount < maxRetry);

    return response;
  }
}
