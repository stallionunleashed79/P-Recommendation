package com.cat.digital.reco.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationUsersEntity {
  @Id
  @Column(name = "catrecid")
  private String catrecid;
  @Basic
  @Column(name = "first_name")
  private String firstName;
  @Basic
  @Column(name = "last_name")
  private String lastName;
  @Basic
  @Column(name = "cws_id")
  private String cwsId;
}
