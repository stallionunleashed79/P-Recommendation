package com.cat.digital.reco.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  @GetMapping("/v1/health")
  public ResponseEntity getHealth() {
    return ResponseEntity.ok("RECO API instance is healthy");
  }
}
