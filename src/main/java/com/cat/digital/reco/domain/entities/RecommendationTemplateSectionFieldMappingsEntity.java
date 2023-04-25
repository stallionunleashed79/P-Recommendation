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

import com.cat.digital.reco.domain.entities.pk.RecommendationTemplateSectionFieldMappingsEntityPk;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_template_section_field_mappings")
@IdClass(RecommendationTemplateSectionFieldMappingsEntityPk.class)
@Data
@NoArgsConstructor
public class RecommendationTemplateSectionFieldMappingsEntity {
  @Id
  @Column(name = "template_section_id")
  private int templateSectionId;

  @Id
  @Column(name = "field_id")
  private int fieldId;

  @Basic
  @Column(name = "is_field_required")
  private boolean isFieldRequired;

  @Basic
  @Column(name = "is_read_only")
  private boolean isReadOnly;

  @Basic
  @Column(name = "default_value")
  private String defaultValue;

  @Basic
  @Column(name = "field_position_number")
  private Integer fieldPositionNumber;

  @Basic
  @Column(name = "min_length")
  private Integer minLength;

  @Basic
  @Column(name = "max_length")
  private Integer maxLength;

  @Basic
  @Column(name = "min_property_value")
  private String minPropertyValue;

  @Basic
  @Column(name = "max_property_value")
  private String maxPropertyValue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_section_id", insertable = false, updatable = false)
  private RecommendationTemplateSectionsEntity templateSection;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "field_id", insertable = false, updatable = false)
  private RecommendationFieldsEntity recommendationField;
}
