package com.cat.digital.reco.stepDefinitions;

import com.cat.digital.reco.RecoApi;
import com.cat.digital.reco.dao.DealerDetailsDao;
import com.cat.digital.reco.dao.NotesDao;
import com.cat.digital.reco.dao.NotificationDao;
import com.cat.digital.reco.dao.StorageDao;
import com.cat.digital.reco.repositories.AssetDetailsRepository;
import com.cat.digital.reco.repositories.RecommendationTemplateRepository;
import com.cat.digital.reco.service.AuditService;
import com.cat.digital.reco.service.PublishService;
import com.cat.digital.reco.service.RecommendationService;
import com.cat.digital.reco.utils.IntegrationTestMapper;
import com.cat.digital.reco.utils.rest.AccessTokenAuthenticator;
import com.cat.digital.reco.utils.rest.OkResterImpl;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

@SpringBootTest(classes = RecoApi.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient
@CucumberContextConfiguration
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class})

public class SpringContextLoader {
    @Autowired
    Environment environment;

//    @MockBean
//    NotificationDao notificationDao;
//    @MockBean
//    RecommendationService recommendationService;
//    @MockBean
//    RecommendationTemplateRepository recommendationTemplateRepository;
//    @MockBean
//    AssetDetailsRepository assetDetailsRepository;
//    @MockBean
//    DealerDetailsDao dealerDetailsDao;
//    @MockBean
//    OkResterImpl okRester;
//    @MockBean
//    NotesDao notesDao;
//    @MockBean
//    StorageDao storageDao;
//    @MockBean
//    AuditService auditService;
//    @MockBean
//    PublishService publishService;

    @Before
    public void setup_cucumber_spring_context() {
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
        //TODO define the properties per each environment
        var authServerHost = environment.getProperty("functionalTest.authServerHost");
        var loginServerHost = environment.getProperty("functionalTest.loginServerHost");
        var clientId = environment.getProperty("functionalTest.clientId");
        var redirectUrl = environment.getProperty("functionalTest.redirectUrl");
        var cwsId = environment.getProperty("functionalTest.cwsId");
        var cwsPassword = environment.getProperty("functionalTest.cwsPassword");

        var accessToken = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjM1IiwicGkuYXRtIjoiOCJ9.eyJzY29wZSI6WyJtYW5hZ2U6YWxsIl0sImNsaWVudF9pZCI6IkZvcmVzaWdodF9hY19jbGllbnQiLCJhY2Nlc3NfZ3JhbnRfZ3VpZF9jY2RzIjoiRGw3MURPaDdrOGxKQmVDSURYMklXTkVOeDNWVUxIc1AiLCJpc3MiOiJodHRwczovL2ZlZGxvZ2lucWEuY2F0LmNvbSIsImF1ZCI6IkNDRFMiLCJjYXRhZmx0bm9yZ2NvZGUiOiJaSDU2NEwwIiwiY2F0bG9naW5pZCI6InNlc2hhYjIiLCJjYXRyZWNpZCI6IlFQUy0wMDAxQUE5QiIsImNhdGN1c3RpZCI6IlpINTY0TDAiLCJjYXRhZmx0bmNvZGUiOiIwMTQiLCJleHAiOjE2MTA0NzE1MDJ9.GMkUpQFheaRpcNssh9Dwt1wU-oG6fh8bYgStTMIl4e0BqUceivl7U2-XnyYmIF67E7FxjYLh4EM_MmfV_nvwr1btBNWEAFZRvdbPW4QY3UpLfZCzv0vT7lF9qj1PCd-QjkX2Y9Fx0_LncRI44eYDITye9BJXKqdTaMdnvHvVoi5azDmLNSoWFt3-F-EhiFRL9d0FiCKGTnIKsrlxtcyqn5v8XY3ugF3wJy-eLq9B_V4yIi6N4zrDdpLz74qNHda5bbTrxp66fwkzC97T1Iu84NyFzuh-uIjWK95DtX6E4lOcjimCNd63rqti_RlS8gmHIlIhX8gijh4_MSGZwJdRSA";
        IntegrationTestMapper.setAccessToken(accessToken);
        IntegrationTestMapper.setEnvironment(environment);
    }
}
