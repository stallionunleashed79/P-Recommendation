package com.cat.digital.reco.config;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.cat.digital.reco.entitlement.EntitlementAuthFilter;
import com.cat.digital.reco.entitlement.EntitlementUtils;
import com.cat.digital.reco.utils.rest.OkResterImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Log4j2
@TestConfiguration
@Configuration
public class BeanConfiguration {

  @Value("${cat.recommendation.number.generator.algorithm:SHA1PRNG}")
  String generatorAlgorithm;

  @Value("${Okhttp.connection.timeout.seconds}")
  private int connectionTimeOut;

  @Value("${Okhttp.connection.read.timeout.seconds}")
  private int connectionReadTimeOut;

  @Value("${Okhttp.logging.request}")
  private boolean isLogRequestEntity;

  @Value("${Okhttp.logging.response}")
  private boolean isLogResponseEntity;

  @Value("${Okhttp.retry.maximum}")
  private int maxRetry;

  @Value("${global.proxy.enable}")
  private boolean proxy;

  // UTILS
  @Bean
  EntitlementUtils entitlementUtils() {
    return new EntitlementUtils();
  }

  @Bean (name = "resterForInternalService")
  OkResterImpl resterForInternalService(@Value("${global.dealer.region}") final String region) {
    return new OkResterImpl(connectionTimeOut, connectionReadTimeOut, maxRetry, isLogRequestEntity, isLogResponseEntity, region, proxy);
  }

  @Bean (name = "resterForExternalService")
  @Primary
  OkResterImpl resterForExternalService() {
    return new OkResterImpl(connectionTimeOut, connectionReadTimeOut, maxRetry, isLogRequestEntity, isLogResponseEntity, proxy);
  }

  @Bean
  EntitlementAuthFilter entitlementAuthFilter(final EntitlementUtils entitlementUtils) {
    return new EntitlementAuthFilter(entitlementUtils);
  }

  @Bean
  SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
    try {
      return SecureRandom.getInstance(generatorAlgorithm);
    } catch (NoSuchAlgorithmException e) {
      log.error("cannot create an instance for secure random, the algorithm {} not found, we use SHA1PRNG", generatorAlgorithm);
      return SecureRandom.getInstance("SHA1PRNG");
    }
  }
}
