package com.cat.digital.reco.domain.entities.pk;

import javax.persistence.Column;
import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecommendationAuditsEntityPK implements Serializable {
  @Column(name = "recommendation_id")
  Integer recommendationId;

  @Column(name = "recommendation_field_id")
  Integer recommendationFieldId;

  @Column(name = "id")
  int id;
}
