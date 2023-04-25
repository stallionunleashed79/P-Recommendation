package com.cat.digital.reco.utils.rest;

import com.cat.digital.reco.entitlements.helper.EntitlementValue;
import okhttp3.*;
import org.apache.http.HttpHeaders;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.cat.digital.reco.common.Constants.ACCEPT;
import static com.cat.digital.reco.common.Constants.X_CAT_ENTITLEMENTS_HEADER;

public class RestClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient okHttpClient;
    private String accessToken;

    public RestClient() {
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(10))
                .build();
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public RecoResponse makeRequest(Request.Builder requestBuilder, String path, EntitlementValue entitlementHeaderValue, HttpUrl.Builder builder, String host) {
        requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        if (!Objects.isNull(entitlementHeaderValue)) {
          requestBuilder.header("x-cat-entitlements", entitlementHeaderValue.getValue());
        }
        var builderOptional = Optional.ofNullable(builder);
        builderOptional.ifPresentOrElse(
            urlElement -> requestBuilder
                .url(urlElement.host(host).addPathSegments(path).build()
                ),
            () -> requestBuilder.url(host+ "/" + path)
        );
        requestBuilder
                .header("Content-Type", "application/json");

        try {
            Response response = okHttpClient.newCall(requestBuilder.build()).execute();
            return new RecoResponse(response.code(), Objects.requireNonNull(response.body()).string());
        } catch (Exception ioe) {
            // interceptor will log.
            System.out.println(ioe.getMessage());
            return null;
        }
    }

    public RecoResponse makeRequestQueryParam(Request.Builder requestBuilder, String path, EntitlementValue entitlementHeaderValue,
                                              HttpUrl.Builder builder, Map<String, String> queryParams, String host, String acceptHeader) {
        requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        if (!Objects.isNull(entitlementHeaderValue)) {
            requestBuilder.header(X_CAT_ENTITLEMENTS_HEADER, entitlementHeaderValue.getValue());
            requestBuilder.header(ACCEPT, acceptHeader);
        }
        var builderOptional = Optional.ofNullable(builder);
        builderOptional.ifPresentOrElse(
                urlElement -> {
                    HttpUrl.Builder url = HttpUrl.parse(host + "/" + path).newBuilder();
                    queryParams.entrySet().forEach(
                            queryParam -> url
                                    .setQueryParameter(queryParam.getKey(), queryParam.getValue()));
                    requestBuilder.url(url.build());
                },
                () -> requestBuilder.url(host + "/" + path)
        );

        requestBuilder
                .header("Content-Type", "application/json");
        try {
            Response response = okHttpClient.newCall(requestBuilder.build()).execute();
            return new RecoResponse(response.code(), Objects.requireNonNull(response.body()).string());
        } catch (Exception ioe) {
            // interceptor will log.
            System.out.println(ioe.getMessage());
            return null;
        }
    }

}
