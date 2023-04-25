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
@Table(name = "recommendation_template_section_container_types")
@Data
@NoArgsConstructor
public class RecommendationTemplateSectionTypesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "section_container_type_id")
  private int sectionContainerTypeId;

  @Basic
  @Column(name = "section_container_type_name")
  private String sectionTypeName;
}
