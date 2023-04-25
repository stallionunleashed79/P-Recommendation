package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_field_collections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationFieldCollectionsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "collection_id")
  private int collectionId;
  @Basic
  @Column(name = "collection_name")
  private String collectionName;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "collection_id", insertable = false, updatable = false)
  private List<RecommendationCollectionOptionsEntity> collectionOptions;
}
