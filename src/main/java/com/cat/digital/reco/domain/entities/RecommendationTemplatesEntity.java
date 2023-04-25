package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationTemplatesEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "template_id")
  private int templateId;

  @Basic
  @Column(name = "template_name")
  private String templateName;

  @Basic
  @Column(name = "description")
  private String description;

  @ManyToMany
  @JoinTable(name = "recommendation_template_section_mappings",
      joinColumns = { @JoinColumn(name = "template_id") },
      inverseJoinColumns = { @JoinColumn(name = "template_section_id") })
  private Set<RecommendationTemplateSectionsEntity> templateSections = new HashSet<>();
}
