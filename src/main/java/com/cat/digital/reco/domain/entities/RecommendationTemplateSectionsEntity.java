package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_template_sections")
@Data
@NoArgsConstructor
public class RecommendationTemplateSectionsEntity {
  @Id
  @Column(name = "template_section_id")
  private int templateSectionId;

  @Basic
  @Column(name = "section_name")
  private String sectionName;

  @Basic
  @Column(name = "section_position_number")
  private Integer sectionPositionNumber;

  @Basic
  @Column(name = "section_container_type_id")
  private int templateSectionTypeId;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "section_container_type_id", insertable = false, updatable = false, referencedColumnName = "section_container_type_id")
  private RecommendationTemplateSectionTypesEntity recommendationTemplateSectionTypesEntity;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_section_id", referencedColumnName = "template_section_id", insertable = false, updatable = false)
  private List<RecommendationTemplateSectionFieldMappingsEntity> templateSectionMappings;
}
