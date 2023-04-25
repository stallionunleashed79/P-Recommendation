package com.cat.digital.reco.entitlement;

import static com.cat.digital.reco.common.Constants.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import com.cat.digital.reco.exceptions.AuthorizationException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class EntitlementAuthFilter implements Filter {
  private EntitlementUtils entitlementUtils;

  @SneakyThrows
  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response,
                       final FilterChain chain) {
    final HttpServletRequest req = (HttpServletRequest) request;
    if (!req.getRequestURI().endsWith(RECOMMENDATIONS_HEALTH_CHECK_URL_REGEX)) {
      var entitlementHeader = req.getHeader(X_CAT_ENTITLEMENTS_HEADER);
      if (Objects.isNull(entitlementHeader) || entitlementHeader.isEmpty()) {
        var servletResponse = (HttpServletResponse) response;
        servletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), NOT_AUTHORIZED_ENTITLEMENT_TOKEN_MISSING_MSG);
        return;
      }
      try {
        final var authorization = entitlementUtils.getEntitlements(entitlementHeader, req);
        req.setAttribute("EntitledDealers", authorization);
      } catch (AuthorizationException ex) {
        var servletResponse = (HttpServletResponse) response;
        servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, NOT_HEADER_FORBIDDEN_MSG);
        return;
      }
    }
    chain.doFilter(req, response);
  }
}
