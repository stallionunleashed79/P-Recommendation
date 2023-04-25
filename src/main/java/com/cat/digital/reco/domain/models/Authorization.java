package com.cat.digital.reco.domain.models;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorization {
  private String catRecId;
  private Set<String> authorizedDealers;
  private boolean isDealer;
}
