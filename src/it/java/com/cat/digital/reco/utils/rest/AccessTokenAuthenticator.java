package com.cat.digital.reco.utils.rest;

import java.io.IOException;
import java.util.Objects;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Log4j2
public class AccessTokenAuthenticator {

  private static final String LOGIN_SERVER_ENDPOINT = "https://%s.cat.com/CwsLogin/cws/processlogin.htm";
  private static final String AUTHORIZATION_SERVER_ENDPOINT = "https://%s.cat.com/as/authorization.oauth2?pfidpadapterid=OAuthAdapterCCDS&client_id=%s&response_type=token&redirect_uri=%s";

  public static String getAccessToken(String authorizationServerHost, String loginServerHost, String clientId, String redirectUrl,
                                      String cwsId, String cwsPassword) {

    setProxy();
    String accessToken = "";
    String authorizationResponseBody = null;
    try {
      Response getSSOCookieResponse = RestAssured.given().formParam("cwsUID", cwsId).formParam("cwsPwd", cwsPassword)
          .post(String.format(LOGIN_SERVER_ENDPOINT, loginServerHost));

      Cookie SSOCookie = getSSOCookieResponse.getDetailedCookie("SSOCookie");

      if (!Objects.isNull(SSOCookie)) {
        String authorizationUrl = String.format(AUTHORIZATION_SERVER_ENDPOINT, authorizationServerHost, clientId, redirectUrl);
        Request request = new Request.Builder().addHeader("Cookie", SSOCookie.toString()).url(authorizationUrl)
            .build();
        authorizationResponseBody = new OkHttpClient().newCall(request).execute().request().url().toString();
        String code = authorizationResponseBody.substring(
                authorizationResponseBody.indexOf("=") + 1,
                authorizationResponseBody.length() - 3);
        accessToken = code.substring(0, code.length() - 31);
      }
    } catch (IOException e) {
      log.error(
          "Unable to get the Authorization Code for cwsid with first char {}, cwsid rest char {} and authorization response is {}",
          cwsId.charAt(0), cwsId.substring(1), authorizationResponseBody);
      log.error(e.getMessage());
      log.error(ExceptionUtils.getStackTrace(e));
      return accessToken;
    }
    return accessToken;
  }

  private static void setProxy() {
    System.setProperty("http.proxyHost", "proxy.cat.com");
    System.setProperty("http.proxyPort", "80");
    System.setProperty("http.nonProxyHosts", "*.cat.com");
    System.setProperty("https.proxyHost", "proxy.cat.com");
    System.setProperty("https.proxyPort", "80");
    System.setProperty("https.nonProxyHosts", "*.cat.com");
  }
  }
