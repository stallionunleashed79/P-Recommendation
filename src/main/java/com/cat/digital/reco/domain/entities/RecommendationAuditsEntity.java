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

import java.sql.Timestamp;

import com.cat.digital.reco.domain.entities.pk.RecommendationAuditsEntityPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(RecommendationAuditsEntityPK.class)
@Table(name = "recommendation_audits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationAuditsEntity {

  @Id
  @Basic
  @Column(name = "recommendation_id")
  private Integer recommendationId;

  @Id
  @Basic
  @Column(name = "recommendation_field_id")
  private Integer recommendationFieldId;

  @Id
  @Basic
  @Column(name = "id")
  private Integer id;

  @Basic
  @Column(name = "old_value")
  private String oldValue;
  @Basic
  @Column(name = "new_value")
  private String newValue;
  @Basic
  @Column(name = "modified_by_catrecid")
  private String modifyBy;
  @Basic
  @Column(name = "modified_date")
  private Timestamp modifiedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recommendation_id", insertable = false, updatable = false)
  private RecommendationCommonDataEntity recommendationHeader;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recommendation_field_id", referencedColumnName = "field_id", insertable = false, updatable = false)
  private RecommendationFieldsEntity recommendationField;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "modified_by_catrecid", referencedColumnName = "catrecid", insertable = false, updatable = false)
  private RecommendationUsersEntity modifiedBy;
}
