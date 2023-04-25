package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asset_details")
@Data
@NoArgsConstructor
public class AssetDetailsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "asset_details_id")
  private int assetDetailsId;

  @Basic
  @Column(name = "serial_number")
  private String serialNumber;

  @Basic
  @Column(name = "make")
  private String make;

  @Basic
  @Column(name = "model")
  private String model;

  @Basic
  @Column(name = "primary_customer_number")
  private String primaryCustomerNumber;

  @Basic
  @Column(name = "primary_customer_name")
  private String primaryCustomerName;

  @Basic
  @Column(name = "dealer_code")
  private String dealerCode;

  @Basic
  @Column(name = "dealer_name")
  private String dealerName;

  @Basic
  @Column(name = "site")
  private String site;

  @Basic
  @Column(name = "smu")
  private int smu;

  @Basic
  @Column(name = "enabled")
  private boolean enabled;
}

