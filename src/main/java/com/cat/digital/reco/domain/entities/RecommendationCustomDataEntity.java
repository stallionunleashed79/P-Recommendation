package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cat.digital.reco.domain.entities.pk.RecommendationCustomDataEntityPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_custom_data")
@IdClass(RecommendationCustomDataEntityPK.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationCustomDataEntity {
  @Id
  @Column(name = "recommendation_id")
  private int recommendationId;
  @Id
  @Column(name = "field_id")
  private int fieldId;
  @Basic
  @Column(name = "value")
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recommendation_id", insertable = false, updatable = false)
  private RecommendationCommonDataEntity recommendationHeader;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "field_id", insertable = false, updatable = false)
  private RecommendationFieldsEntity recommendationField;
}
