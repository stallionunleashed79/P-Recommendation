package com.cat.digital.reco.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.cat.digital.reco.domain.models.BaseOwner;
import com.cat.digital.reco.domain.models.HoursReading;
import com.cat.digital.reco.domain.models.TemplateCustomField;
import com.cat.digital.reco.entitlements.helper.EntitlementValue;
import com.cat.digital.reco.stepDefinitions.UpdateRecoImpl;
import com.cat.digital.reco.utils.rest.RecoResponse;
import com.cat.digital.reco.utils.rest.RestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groovy.text.Template;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Map;

public class IntegrationTestMapper {
    public static RecoResponse response;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final RestClient restClient = new RestClient();
    private static String URL_HOST = "localhost";


    public static void setEnvironment(Environment environment){
        URL_HOST = environment.getProperty("functionalTest.url");
    }

    public static void setAccessToken(String tokenData) {
        restClient.setAccessToken(tokenData);
    }

    public static String getAccessToken() {
        return restClient.getAccessToken();
    }

    public static void makeRequest(Request.Builder requestBuilder, String path, EntitlementValue entitlementHeaderValue, HttpUrl.Builder url) {
        response = restClient.makeRequest(requestBuilder, path, entitlementHeaderValue, url, URL_HOST);
    }

    public static void makeRequestQueryParam(Request.Builder requestBuilder, String path, EntitlementValue entitlementHeaderValue, HttpUrl.Builder url, Map queryparam, String acceptHeader) {
        response = restClient.makeRequestQueryParam(requestBuilder, path, entitlementHeaderValue, url, queryparam, URL_HOST, acceptHeader);
    }

    public static void makeGetRequest(String path, EntitlementValue entitlementHeaderValue, HttpUrl.Builder url) {
        Request.Builder requestBuilder = new Request.Builder().get();
        response = restClient.makeRequest(requestBuilder, path, entitlementHeaderValue, url, URL_HOST);
    }

    public static void makeDeleteRequest(String path, EntitlementValue entitlementHeaderValue, HttpUrl.Builder url) {
        Request.Builder requestBuilder = new Request.Builder().delete();
        response = restClient.makeRequest(requestBuilder, path, entitlementHeaderValue, url, URL_HOST);
    }

    @SneakyThrows
    public static String toJson(Object obj) throws JsonProcessingException {
        configureObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private static void configureObjectMapper(){
        objectMapper
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
    }

    @SneakyThrows
    public static <T> T fromJson(String string, TypeReference<T> type) throws IOException {
        return objectMapper.readValue(string, type);
    }

    public static <T> T convertValue(Object in, Class<T> out) {
        return objectMapper.convertValue(in, out);
    }

    /**
     * Build template custom fields from input request table row.
     * @param row The table row of input request
     * @return the list of template custom fields
     */
    public static List<TemplateCustomField> getTemplateCustomFieldsFromTableRow(final Map<String, String> row) {
        var templateFieldsList = new ArrayList<TemplateCustomField>();
        String propertyName = null;
        String propertyValue = null;
        TemplateCustomField customField = null;
        for (var key: row.keySet()) {
            if (key.toLowerCase().contains("propertyName".toLowerCase())) {
                propertyName = row.get(key);
            }
            if (key.toLowerCase().contains("propertyValue".toLowerCase())) {
                propertyValue = row.get(key);
            }
            if (!StringUtils.isBlank(propertyName) && !StringUtils.isBlank(propertyValue)) {
                templateFieldsList.add(TemplateCustomField.builder().propertyName(propertyName)
                    .propertyValue(propertyValue).build());
                propertyName = null;
                propertyValue = null;
            }
        }
        return templateFieldsList;
    }

    /**
     * Build hours reading from input request table row.
     * @param row The table row of input request
     * @return the hours reading object
     */
    public static HoursReading getHoursReadingFromTableRow(Map<String, String> row) {
        return HoursReading.builder().reading(
            new BigDecimal(row.get("reading"))).unitOfMeasure(
                row.get("unitOfMeasure")).build();
    }

    /**
     * Build base owner from input request table row.
     * @param row The table row of input request
     * @return the base owner
     */
    public static BaseOwner getBaseOwnerFromTableRow(Map<String, String> row) {
        return BaseOwner.builder().catrecid(row.get("catrecId")).build();
    }

    /**
     * Removes a required field from input request from common or custom properties section
     * @param requiredField The required field to remove
     * @param recommendationRequest The Input request
     * @param <T> Parameterized type
     * @return the modified request after removal of required field
     * @throws JsonProcessingException
     */
    public static <T> JSONObject removeRequiredFieldFromRequest(final String requiredField, final T recommendationRequest) throws JsonProcessingException {
      var recommendationJsonObject = new JSONObject(toJson(recommendationRequest));
      if (recommendationJsonObject.has(requiredField)) {
          recommendationJsonObject.remove(requiredField);
          return recommendationJsonObject;
      }
      var customPropertiesSection = (JSONArray)recommendationJsonObject.get(UpdateRecoImpl.TEMPLATE_CUSTOM_PROPERTIES);
      var filteredSection =  new JSONArray(customPropertiesSection.toList().stream().filter(field -> {
            var propertyEntry = (HashMap)field;
            var fieldName = (String)propertyEntry.get("propertyName");
            return !fieldName.equalsIgnoreCase(requiredField);
        }).collect(Collectors.toList()));
        recommendationJsonObject.put(UpdateRecoImpl.TEMPLATE_CUSTOM_PROPERTIES, filteredSection);
        return recommendationJsonObject;
    }
}
