package com.cat.digital.reco.domain.entities.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecommendationTemplateSectionFieldMappingsEntityPk implements Serializable {

  @Id
  @Column(name = "template_section_id")
  private int templateSectionId;

  @Id
  @Column(name = "field_id")
  private int fieldId;
}
