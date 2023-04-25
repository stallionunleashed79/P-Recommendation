package com.cat.digital.reco.dao.impl;

import java.io.IOException;
import java.util.Objects;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.dao.UserManagementDao;
import com.cat.digital.reco.domain.models.UserManagementData;
import com.cat.digital.reco.exceptions.EntityNotFoundException;
import com.cat.digital.reco.utils.rest.OkResterImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UserManagementDaoImpl implements UserManagementDao {

  @Value("${client.user.management.url:https://services.cat.com/catDigital/userManagement/v1}")
  private String userManagementUrl;

  @Value("${client.user.management.path:/users/}")
  private String path;

  @Value("${client.user.management.replace:{catRecId}}")
  private String catRecIdReplace;

  private final ObjectMapper objectMapper;

  private final OkResterImpl okHttpClient;

  @Autowired
  public UserManagementDaoImpl(ObjectMapper objectMapper, OkResterImpl okHttpClient) {
    this.objectMapper = objectMapper;
    this.okHttpClient = okHttpClient;
  }


  @Override
  public UserManagementData getUserData(String catRecId) throws IOException {

    var clientURL = String.format("%s%s%s", userManagementUrl, path, catRecIdReplace).replace(catRecIdReplace, catRecId);

    log.info(String.format("Service URL: %s", clientURL));

    Request request = new Request.Builder().url(clientURL).get().build();

    try (Response response = okHttpClient.makeRequest(request)) {
      if (response.isSuccessful() && !Objects.isNull(response.body())) {
        return objectMapper.readValue(response.body().string(), UserManagementData.class);
      } else {
        throw new EntityNotFoundException(CustomResponseCodes.INTERNAL_SERVER_ERROR, String.format("cannot get response form user management API for %s user, service response: %s", catRecId, response.message()));
      }
    }
  }
}
