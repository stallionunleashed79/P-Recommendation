package com.cat.digital.reco.domain.entities.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecommendationCustomDataEntityPK implements Serializable {
  @Column(name = "recommendation_id")
  @Id
  private int recommendationId;
  @Column(name = "field_id")
  @Id
  private int fieldId;
}
