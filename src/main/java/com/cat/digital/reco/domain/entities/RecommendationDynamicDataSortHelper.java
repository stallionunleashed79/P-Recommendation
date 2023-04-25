package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_dynamic_sort_helper")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationDynamicDataSortHelper {

  @Id
  @Basic
  @Column(name = "recommendation_id")
  private Integer recommendationId;

  @Basic
  @Column(name = "recommendation_number")
  private String recommendationNumber;

  @Basic
  @Column(name = "recommendation_status")
  private String recommendationStatus;

  @Basic
  @Column(name = "recommendation_priority")
  private String recommendationPriority;

  @Basic
  @Column(name = "work_order_number")
  private String workOrderNumber;

  @Basic
  @Column(name = "is_dealer_recommendation")
  private boolean dealerRecommendation;

  @Basic
  @Column(name = "dealer_code")
  private String dealerCode;

  @Basic
  @Column(name = "serial_number")
  private String serialNumber;

  @Basic
  @Column(name = "title")
  private String title;

  @Basic
  @Column(name = "site")
  private String site;

  @Basic
  @Column(name = "asset_name")
  private String assetId;

  @Basic
  @Column(name = "customer_ucid")
  private String ucidName;

  @Basic
  @Column(name = "created_by_catrecid")
  private String creatorName;

  @Basic
  @Column(name = "owner")
  private String owner;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recommendation_id", insertable = false, updatable = false)
  private RecommendationCommonDataEntity recommendationCommonData;
}
