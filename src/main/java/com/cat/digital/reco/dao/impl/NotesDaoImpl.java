package com.cat.digital.reco.dao.impl;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.dao.NotesDao;
import com.cat.digital.reco.domain.models.RecommendationLink;
import com.cat.digital.reco.exceptions.RecoServerException;
import com.cat.digital.reco.utils.rest.OkResterImpl;
import lombok.extern.log4j.Log4j2;
import okhttp3.Request;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class NotesDaoImpl implements NotesDao {
  private final OkResterImpl okHttpClient;

  private final String url;
  private final String path;
  private final String maxNotesPerLevel;
  private final String maxDepth;

  @Autowired
  public NotesDaoImpl(@Qualifier("resterForExternalService") OkResterImpl okHttpClient,
                      @Value("${client.notes.url}") final String url,
                      @Value("${client.notes.path}") final String path,
                      @Value("${client.notes.maxNotesPerLevel}") final String maxNotesPerLevel,
                      @Value("${client.notes.maxDepth}") final String maxDepth) {
    this.okHttpClient = okHttpClient;
    this.url = url;
    this.path = path;
    this.maxNotesPerLevel = maxNotesPerLevel;
    this.maxDepth = maxDepth;
  }

  public List<RecommendationLink> getLinks(String recommendationNumber) {
    var notesUrl = String.format(
        "%s%s?referenceId=%s&maxNotesPerLevel=%s&maxDepth=%s",
        url,
        path,
        recommendationNumber,
        maxNotesPerLevel,
        maxDepth
        );

    var request = new Request.Builder().get().url(notesUrl).build();
    var links = new ArrayList<RecommendationLink>();

    try (var response = okHttpClient.makeRequest(request)) {
      var json = response.body().string();
      var jsonObject = new JSONObject(json);
      var jsonArray = jsonObject.getJSONArray("notes");

      int length = jsonArray.length();
      for (int index = 0; index < length; index++) {
        var note = jsonArray.getJSONObject(index);
        var link = new RecommendationLink();
        link.setNoteId(note.getString("id"));
        link.setUrl(note.getString("content"));
        link.setAttachedTime(OffsetDateTime.parse(note.getString("createTime")));
        links.add(link);
      }
    } catch (IOException | JSONException exception) {
      log.error("Recommendation {} failed to get links due to {}", recommendationNumber, exception.getStackTrace());
      /**
       * TODO: REMOVING THE SECTION OF CODE THAT THROWS AN INTERNAL SERVER ERROR FOR NOW AS A
       * TEMPORARY FIX TO ALLOW THE UI TO FETCH RECOMMENDATIONS EVEN IF THEY DO NOT HAVE ENTITLEMENTS
       * TO NOTES/ATTACHMENTS FOR NOW BUT THIS SECTION THAT THROWS INTERNAL SERVER ERROR
       * NEEDS TO BE PUT BACK ONCE THEY HAVE THE ENTITLEMENTS FOR NOTES/ATTACHMENTS
       */
    }
    return !links.isEmpty() ? links : null;
  }
}
