package com.cat.digital.reco.interceptors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * interceptor used to extract the x-cat-authorization header and setting in the security context.
 */
public class AuthenticationInterceptor extends OncePerRequestFilter {

  /**
   * Get x-cat-authorization header, if is not present the request will be rejected.
   * @see OncePerRequestFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    SecurityContextHolder.clearContext();
    Optional<String> authorizationHeader = getAuthorizationHeader(request);

    authorizationHeader.ifPresent(auth -> {
      var authorizationData = new PreAuthenticatedAuthenticationToken(auth, null, null);
      SecurityContextHolder.getContext().setAuthentication(authorizationData);
    });

    if (authorizationHeader.isEmpty()) {
      response.sendError(HttpStatus.UNAUTHORIZED.value());
      return;
    }
    filterChain.doFilter(request, response);
  }

  private Optional<String> getAuthorizationHeader(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
  }
}
