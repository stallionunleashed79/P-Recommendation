package com.cat.digital.reco.utils.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * intercept the service call request and attach the authorization header from our request.
 */
public class AccessTokenInterceptor implements Interceptor {
  @Override
  public Response intercept(Chain chain) throws IOException {
    var accessToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Request request = newRequestWithAccessToken(chain.request(), accessToken);
    return chain.proceed(request);
  }

  private Request newRequestWithAccessToken(Request request, String accessToken) {
    return request.newBuilder()
        .header(HttpHeaders.AUTHORIZATION, accessToken)
        .build();
  }
}
