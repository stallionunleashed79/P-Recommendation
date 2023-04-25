package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_common_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationCommonDataEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "recommendation_id")
  private int recommendationId;
  @Basic
  @Column(name = "recommendation_number")
  private String recommendationNumber;
  @Basic
  @Column(name = "title")
  private String title;
  @Basic
  @Column(name = "smu")
  private int smu;
  @Basic
  @Column(name = "owner")
  private String owner;
  @Basic
  @Column(name = "asset_name")
  private String assetName;
  @Basic
  @Column(name = "site")
  private String site;
  @Basic
  @Column(name = "expiration_date")
  private Timestamp expirationDate;
  @Basic
  @Column(name = "created_date")
  private Timestamp createdDate;
  @Basic
  @Column(name = "updated_date")
  private Timestamp updatedDate;
  @Basic
  @Column(name = "created_by_catrecid")
  private String createdByCatrecid;
  @Basic
  @Column(name = "updated_by_catrecid")
  private String updatedByCatrecid;
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
  @Column(name = "dealer_code")
  private String dealerCode;
  @Basic
  @Column(name = "dealer_name")
  private String dealerName;
  @Basic
  @Column(name = "customer_ucid")
  private String customerUcid;
  @Basic
  @Column(name = "customer_name")
  private String customerName;
  @Basic
  @Column(name = "attachment_size")
  private BigDecimal attachmentSize;
  @Basic
  @Column(name = "is_dealer_recommendation")
  private boolean dealerRecommendation;

  @OneToMany(mappedBy = "recommendationHeader", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  private List<RecommendationCustomDataEntity> recommendationDetails;

  @OneToMany(mappedBy = "recommendationHeader", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  private List<RecommendationAuditsEntity> recommendationAudits;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_id")
  private RecommendationTemplatesEntity template;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", referencedColumnName = "catrecid", insertable = false, updatable = false)
  private RecommendationUsersEntity ownedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by_catrecid", referencedColumnName = "catrecid", insertable = false, updatable = false)
  private RecommendationUsersEntity createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "updated_by_catrecid", referencedColumnName = "catrecid", insertable = false, updatable = false)
  private RecommendationUsersEntity updatedBy;
}
