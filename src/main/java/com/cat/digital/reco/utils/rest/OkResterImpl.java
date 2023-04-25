package com.cat.digital.reco.utils.rest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Log4j2
public class OkResterImpl {

  private static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
  private static final String PROXY_HOST = "proxy.cat.com";
  private static final int PROXY_PORT = 80;
  private static final String SERVICE_NAME = "execute-api";

  private final OkHttpClient okHttpClient;

  public OkResterImpl(final int connectionTimeOut,
                      final int connectionReadTimeOut,
                      final int maxRetry,
                      final boolean isLogRequestEntity,
                      final boolean isLogResponseEntity,
                      final boolean isProxyEnable) {
    log.info("OKRester for external service call...");
    var builder = new OkHttpClient.Builder()
        .connectTimeout(Duration.ofSeconds(connectionTimeOut))
        .readTimeout(Duration.ofSeconds(connectionReadTimeOut))
        .addInterceptor(new AccessTokenInterceptor())
        .addInterceptor(new RetryInterceptor(maxRetry))
        .addInterceptor(new OkHttpLoggingInterceptor(isLogRequestEntity, isLogResponseEntity));

    if (isProxyEnable) {
      log.info("OKRester with proxy configuration setup...");
      builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)));
    }

    this.okHttpClient = builder.build();
  }


  /**
   * construct.
   */
  public OkResterImpl(final int connectionTimeOut,
                      final int connectionReadTimeOut,
                      final int maxRetry,
                      final boolean isLogRequestEntity,
                      final boolean isLogResponseEntity,
                      final String dealerRegion,
                      boolean proxyEnable) {
    log.info("OKRester for internal service call...");
    log.info("OkRester is set for dealerRegion:'{}'", dealerRegion);
    AWS4Signer signer = new AWS4Signer();
    signer.setRegionName(dealerRegion);
    signer.setServiceName(SERVICE_NAME);

    if (proxyEnable) {
      log.info("OKRester with proxy configuration setup...");
      this.okHttpClient = new OkHttpClient.Builder()
          .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)))
          .connectTimeout(Duration.ofSeconds(connectionTimeOut))
          .readTimeout(Duration.ofSeconds(connectionReadTimeOut))
          .addInterceptor(new RetryInterceptor(maxRetry))
          .addInterceptor(new OkHttpLoggingInterceptor(isLogRequestEntity, isLogResponseEntity))
          .addInterceptor(new AWSRequestSigningOkHttpInterceptor(signer, credentialsProvider))
          .build();
    } else {
      log.info("OKRester not proxy configuration...");
      this.okHttpClient = new OkHttpClient.Builder()
          .connectTimeout(Duration.ofSeconds(connectionTimeOut))
          .readTimeout(Duration.ofSeconds(connectionReadTimeOut))
          .addInterceptor(new RetryInterceptor(maxRetry))
          .addInterceptor(new OkHttpLoggingInterceptor(isLogRequestEntity, isLogResponseEntity))
          .addInterceptor(new AWSRequestSigningOkHttpInterceptor(signer, credentialsProvider))
          .build();
    }
  }

  /**
   * Make a GET request.
   *
   * @param request - request
   * @return - response
   */
  public Response makeRequest(final Request request) throws IOException {

    return this.okHttpClient.newCall(request).execute();
  }
}
