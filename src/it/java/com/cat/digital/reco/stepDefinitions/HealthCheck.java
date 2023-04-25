package com.cat.digital.reco.stepDefinitions;

import com.cat.digital.reco.entitlements.helper.EntitlementValue;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import io.cucumber.java.en.When;

public class HealthCheck {
    @When("a user performs a Health check to the application")
    public void healthCheck() {
        IntegrationTestMapper.makeGetRequest("v1/health",
            null,null);
    }
}
