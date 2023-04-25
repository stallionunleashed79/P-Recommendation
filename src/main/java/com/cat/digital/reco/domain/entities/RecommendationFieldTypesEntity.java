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
@Table(name = "recommendation_field_types")
@Data
@NoArgsConstructor
public class RecommendationFieldTypesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "field_type_id")
  private int fieldTypeId;

  @Basic
  @Column(name = "field_type_name")
  private String fieldTypeName;

  @Basic
  @Column(name = "is_system_generated", columnDefinition = "boolean default false")
  private boolean isSystemGenerated;
}
