package com.cat.digital.reco.dao.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.cat.digital.reco.dao.NotificationDao;
import com.cat.digital.reco.domain.requests.RecommendationPublishRequest;
import com.cat.digital.reco.utils.rest.OkResterImpl;

import lombok.extern.log4j.Log4j2;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


@Component
@Log4j2
public class NotificationDaoImpl implements NotificationDao {

  private final String notificationUrl;
  private final String path;
  private final OkResterImpl okHttpClient;

  @Autowired
  public NotificationDaoImpl(@Qualifier("resterForInternalService") OkResterImpl okHttpClient,
      @Value("${client.notification.url}") String notificationUrl,
      @Value("${client.notification.path}") String path) {
    this.okHttpClient = okHttpClient;
    this.notificationUrl = notificationUrl;
    this.path = path;
  }


  @Override
  public Boolean sendNotification(RecommendationPublishRequest publishRequest) throws IOException {
    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("recommendationNumber", "test")
        .addFormDataPart("file", "file.txt",
            RequestBody.create(MediaType.parse("application/octet-stream"), getTestFile().getFile()))
        .build();

    log.info("Service URL " + notificationUrl + path);

    Request request = new Request.Builder()
        .url(notificationUrl + path)
        .post(requestBody)
        .build();

    try (Response response = okHttpClient.makeRequest(request)) {
      return response != null && response.isSuccessful();
    }
  }

  public static Resource getTestFile() throws IOException {
    Path testFile = Files.createTempFile("test-file", ".txt");
    log.info("Creating and Uploading Test File: " + testFile);
    Files.write(testFile, "Hello World !!, This is a test file.".getBytes());
    return new FileSystemResource(testFile.toFile());
  }

}
