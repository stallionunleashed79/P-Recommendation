package com.cat.digital.reco.domain.models;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "Model class for asset details")
@Data
public class AssetDetails {
  private int assetDetailsId;
  private String serialNumber;
  private String make;
  private String model;
  private String primaryCustomerNumber;
  private String primaryCustomerName;
  private String dealerCode;
  private String dealerName;
  private String site;
  private int smu;
  private boolean enabled;
}
