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
@Table(name = "recommendation_payload_types")
@Data
@NoArgsConstructor
public class RecommendationPayloadTypesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payload_type_id")
  private int payloadTypeId;
  @Basic
  @Column(name = "payload_name")
  private String payloadName;
}
