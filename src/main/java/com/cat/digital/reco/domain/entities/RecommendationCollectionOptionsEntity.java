package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_collection_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationCollectionOptionsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "option_id")
  private int optionId;
  @Basic
  @Column(name = "option_name")
  private String optionName;
  @Basic
  @Column(name = "option_value")
  private String optionValue;
  @Basic
  @Column(name = "collection_id")
  private int collectionId;
}
