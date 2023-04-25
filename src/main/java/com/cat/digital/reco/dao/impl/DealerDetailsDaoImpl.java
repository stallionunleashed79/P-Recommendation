package com.cat.digital.reco.dao.impl;

import static com.cat.digital.reco.common.Constants.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cat.digital.reco.dao.DealerDetailsDao;
import com.cat.digital.reco.exceptions.DealerDetailException;
import com.cat.digital.reco.utils.rest.OkResterImpl;
import lombok.extern.log4j.Log4j2;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.cat.digital.reco.common.CustomResponseCodes;

@Component
@Log4j2
public class DealerDetailsDaoImpl implements DealerDetailsDao {

  private final OkResterImpl okHttpClient;
  private String url;
  private String dealerPath;

  @Autowired
  public DealerDetailsDaoImpl(
      @Value("${dealer.url}") final String url,
      @Qualifier("resterForInternalService") final OkResterImpl okHttpClient,
      @Value("${dealer.path}") final String dealerPath) {
    this.url = url;
    this.okHttpClient = okHttpClient;
    this.dealerPath = dealerPath;
  }

  @Override
  public Set<String> getDealerCodes(final List<String> regionCodes) throws IOException, DealerDetailException {
    log.debug("Calling dealer details service to resolve regions.");
    var dealerCodes = new HashSet<String>();
    var response = getResponse(regionCodes);
    if (!response.isSuccessful()) {
      log.error("Failed while requesting dealer codes.");
      throw new DealerDetailException(NOT_AUTHORIZED_DEALER_REQUEST_EXCEPTION_MSG, CustomResponseCodes.NOT_AUTHORIZED_REQUEST_EXCEPTION);
    }
    try {
      var json = response.body().string();
      var jsonObject = new JSONObject(json);
      var jsonArray = jsonObject.getJSONArray("dealers");
      if (jsonArray != null) {
        for (int j = 0; j < jsonArray.length(); j++) {
          dealerCodes.add(jsonArray.getString(j));
        }
      }
    } finally {
      response.close();
    }
    log.debug("Bulk call to generate dealer codes successfully generated.");
    return dealerCodes;
  }

  @Override
  public String addQueryParams(final List<String> regionCodes) {
    var stringBuilder = new StringBuilder();
    stringBuilder.append(url).append(dealerPath).append("?regionCodes=");
    for (String regionCode : regionCodes) {
      log.info("Region code: " + regionCode);
      stringBuilder.append(regionCode + ",");
    }
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    stringBuilder.append("&property=dealerCode");
    log.info("Service URL to call: " + stringBuilder.toString());
    return stringBuilder.toString();
  }

  /**
   * Fetches dealer codes for a region code using the OkHttpClient REST client library.
   * @param regionCodes The region code to fetch dealer codes for
   * @return The dealer code fetched for the region code
   * @throws IOException Throws IOException if there is an exception during the fetch
   */
  private Response getResponse(final List<String> regionCodes) throws IOException {
    log.info("Path: [{}]", addQueryParams(regionCodes));
    var request = new Request.Builder().url(addQueryParams(regionCodes)).get().build();
    return okHttpClient.makeRequest(request);
  }
}
