package com.cat.digital.reco.dao.impl;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.dao.StorageDao;
import com.cat.digital.reco.domain.models.Attachment;
import com.cat.digital.reco.exceptions.RecoServerException;
import com.cat.digital.reco.utils.rest.OkResterImpl;
import lombok.extern.log4j.Log4j2;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class StorageDaoImpl implements StorageDao {

  private final OkResterImpl okHttpClient;
  private final String url;
  private final String path;

  private static final String filterBody = "{"
      + "      \"filters\": ["
      + "        {"
      + "          \"filterKey\": \"referenceId\","
      + "          \"filterValues\": ["
      + "            \"%s\""
      + "          ]"
      + "        }"
      + "      ]"
      + "    }";

  @Autowired
  public StorageDaoImpl(
      @Qualifier("resterForExternalService") OkResterImpl okHttpClient,
      @Value("${client.storage.url}") final String url,
      @Value("${client.storage.path}") final String path) {
    this.url = url;
    this.path = path;
    this.okHttpClient = okHttpClient;
  }

  public List<Attachment> retrieveFileMetadata(String recommendationNumber, String partyNumber) {
    var storageUrl = String.format("%s%s?partyNumber=%s&limit=1000", url, path, partyNumber);

    var filterBodyJSON = String.format(filterBody, recommendationNumber);

    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), filterBodyJSON);

    var request = new Request.Builder().post(requestBody).url(storageUrl).build();
    var attachments = new ArrayList<Attachment>();

    try (var response = okHttpClient.makeRequest(request)) {
      var json = response.body().string();
      var jsonObject = new JSONObject(json);
      var jsonArray = jsonObject.getJSONArray("files");

      int length = jsonArray.length();
      for (int index = 0; index < length; index++) {
        var fileData = jsonArray.getJSONObject(index);
        var attachment = new Attachment();
        if (fileData.getString("status").equals("uploaded")) {
          attachment.setFileId(fileData.getString("id"));
          attachment.setName(fileData.getString("fileName"));
          attachment.setFileAttachedTime(OffsetDateTime.parse(fileData.getString("uploadTime")));
          attachments.add(attachment);
        }
      }
    } catch (IOException | JSONException exception) {
      /**
       * TODO: REMOVING THE SECTION OF CODE THAT THROWS AN INTERNAL SERVER ERROR FOR NOW AS A
       * TEMPORARY FIX TO ALLOW THE UI TO FETCH RECOMMENDATIONS EVEN IF THEY DO NOT HAVE ENTITLEMENTS
       * TO NOTES/ATTACHMENTS FOR NOW BUT THIS SECTION THAT THROWS INTERNAL SERVER ERROR
       * NEEDS TO BE PUT BACK ONCE THEY HAVE THE ENTITLEMENTS FOR NOTES/ATTACHMENTS
       */
      log.error("Recommendation {} failed to get attachments due to {}", recommendationNumber, exception.getStackTrace());
    }
    return !attachments.isEmpty() ? attachments : null;
  }
}
