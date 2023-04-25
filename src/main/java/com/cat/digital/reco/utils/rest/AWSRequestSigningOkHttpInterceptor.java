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

import java.io.IOException;
import java.util.Optional;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.Signer;
import lombok.extern.log4j.Log4j2;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;


/**
 * signs an OkHttp request in AWS.
 */
@Log4j2
public class AWSRequestSigningOkHttpInterceptor implements Interceptor {

  /**
   * The particular signer implementation.
   */
  private final Signer signer;

  /**
   * The source of AWS credentials for signing.
   */
  private final AWSCredentialsProvider awsCredentialsProvider;

  /**
   * Request signer.
   *
   * @param signer                 particular signer implementation
   * @param awsCredentialsProvider source of AWS credentials for signing
   */
  public AWSRequestSigningOkHttpInterceptor(final Signer signer,
                                            final AWSCredentialsProvider awsCredentialsProvider) {
    this.signer = signer;
    this.awsCredentialsProvider = awsCredentialsProvider;
    Optional<AWSCredentials> credentialsOptional = Optional.empty();
    try {
      credentialsOptional = Optional.ofNullable(awsCredentialsProvider).map(AWSCredentialsProvider::getCredentials);
    } catch (Exception e) {
      log.error("AwsCredentialsProvider could not getCredentials (ignore this if this is local, i.e. not on AWS). [{}]", e.getLocalizedMessage(), e);
    }

    credentialsOptional.ifPresentOrElse(
        awsCredentials -> {
          var hasAccessKey = Boolean.toString(!StringUtils.isEmpty(awsCredentials.getAWSAccessKeyId()));
          var hasSecretKey = Boolean.toString(!StringUtils.isEmpty(awsCredentials.getAWSSecretKey()));
          log.debug("AWSRequestSigningOkHttpInterceptor hasAccessKey {}, hasSecretKey {}", hasAccessKey, hasSecretKey);
        },
        () -> log.error("AWSRequestSigningOkHttpInterceptor has no credentials (ignore this if this is local, i.e. not on AWS)"));
  }

  @Override
  public Response intercept(final Chain chain) throws IOException {
    log.info("Interceptor for internal services call...");
    AwsSignableOkHttpRequest request = new AwsSignableOkHttpRequest(chain.request());
    // Sign it
    signer.sign(request, awsCredentialsProvider.getCredentials());
    Request copiedRequest = request.generateRequest();
    return chain.proceed(copiedRequest);
  }
}