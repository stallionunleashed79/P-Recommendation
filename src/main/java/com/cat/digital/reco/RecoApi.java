package com.cat.digital.reco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cat")
public class RecoApi {

  public static void main(String[] args) {
    SpringApplication.run(RecoApi.class, args);
  }

}
