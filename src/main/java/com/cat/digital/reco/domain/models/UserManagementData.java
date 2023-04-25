package com.cat.digital.reco.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementData {
  private String firstName;
  private String lastName;
  private String username;
}
