package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_fields")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationFieldsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "field_id")
  private int fieldId;

  @Basic
  @Column(name = "field_name")
  private String fieldName;

  @Basic
  @Column(name = "display_name")
  private String displayName;

  @Basic
  @Column(name = "is_common_header_field", columnDefinition = "boolean default false")
  private boolean isCommonHeaderField;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payload_type_id")
  @NotNull
  private RecommendationPayloadTypesEntity payloadType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "field_type_id")
  private RecommendationFieldTypesEntity fieldType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "collection_id")
  private RecommendationFieldCollectionsEntity collection;
}
