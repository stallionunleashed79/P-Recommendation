package com.cat.digital.reco.dao;

import java.io.IOException;

import com.cat.digital.reco.domain.models.UserManagementData;

public interface UserManagementDao {
  UserManagementData getUserData(String catRecId) throws IOException;
}
