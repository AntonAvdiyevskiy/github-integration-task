package com.example.vcs.integration;

import com.example.vcs.dto.UserRepositoriesDetailsDTO;
import com.example.vcs.service.exception.ApiExceptionDTO;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.example.vcs.constants.EndpointsConstants.ErrorMessages.RESOURCE_NOT_FOUND_MSG;
import static com.example.vcs.constants.EndpointsConstants.ErrorMessages.RATE_LIMIT_EXCEEDED_MSG;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.forbidden;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.get;


@SpringBootTest(
        classes = com.example.vcs.GithubIntegrationApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock
public class GithubIntegrationTest {
    @LocalServerPort
    private int port;

    @RegisterExtension
    static WireMockExtension wm = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private static HttpHeaders httpHeaders;
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_XML = "application/xml";
    private static final String NOT_SUPPORTED_TYPE = "No acceptable representation";

    @BeforeEach
    public void initializeTestData() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, APPLICATION_JSON);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("github.domain", wm::baseUrl);
    }

    @Test
    public void getRepositories_successfulResponse() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        wm.stubFor(get(urlMatching("/users/.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                  [
                                       {
                                           "name": "-Polytechnic_Project",
                                           "full_name": "AntonAvdiyevskiy/-Polytechnic_Project",
                                           "owner": {
                                               "login": "AntonAvdiyevskiy"
                                           },
                                           "fork": false
                                       }
                                   ]
                                """)));
        wm.stubFor(get(urlMatching("/repos/.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                  [
                                        {
                                            "name": "master",
                                            "commit": {
                                                "sha": "93f75d8c4b2e5b41ac1d0dae4b098ee682c3ebb2",
                                                "url": "https://api.github.com/repos/AntonAvdiyevskiy/-Polytechnic_Project/commits/93f75d8c4b2e5b41ac1d0dae4b098ee682c3ebb2"
                                            },
                                            "protected": false
                                        },
                                        {
                                            "name": "toy_store",
                                            "commit": {
                                                "sha": "2fadb269d81fbe5eefed14e93709083124eaa841",
                                                "url": "https://api.github.com/repos/AntonAvdiyevskiy/-Polytechnic_Project/commits/2fadb269d81fbe5eefed14e93709083124eaa841"
                                            },
                                            "protected": false
                                        }
                                    ]
                                """)));

        ResponseEntity<UserRepositoriesDetailsDTO[]> response = restTemplate.exchange(
                createURLWithPort("/github/user/AntonAvdiyevskiy/repos"), HttpMethod.GET, entity, UserRepositoriesDetailsDTO[].class);

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void getRepositories_NotSupportedAcceptTypeHeader() {
        httpHeaders.set(HttpHeaders.ACCEPT, APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<ApiExceptionDTO> response = restTemplate.exchange(
                createURLWithPort("/github/user/AntonAvdiyevskiy/repos"), HttpMethod.GET, entity, ApiExceptionDTO.class);
        ApiExceptionDTO apiExceptionDTO = response.getBody();

        Assertions.assertEquals(406, (int) apiExceptionDTO.getStatus());
        Assertions.assertEquals(NOT_SUPPORTED_TYPE, apiExceptionDTO.getMessage());

    }

    @Test
    public void getRepositories_UserNotFound() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        wm.stubFor(get(urlMatching("/users/.*"))
                .willReturn(notFound()));

        ResponseEntity<ApiExceptionDTO> response = restTemplate.exchange(
                createURLWithPort("/github/user/AntonAvdiyevskiy1/repos"), HttpMethod.GET, entity, ApiExceptionDTO.class);
        ApiExceptionDTO apiExceptionDTO = response.getBody();

        Assertions.assertEquals(404, (int) apiExceptionDTO.getStatus());
        Assertions.assertEquals(RESOURCE_NOT_FOUND_MSG, apiExceptionDTO.getMessage());
    }

    @Test
    public void getRepositories_APIRateLimitExceeded() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        wm.stubFor(get(urlMatching("/users/.*"))
                .willReturn(forbidden()));

        ResponseEntity<ApiExceptionDTO> response = restTemplate.exchange(
                createURLWithPort("/github/user/AntonAvdiyevskiy/repos?per_page=1"), HttpMethod.GET, entity, ApiExceptionDTO.class);
        ApiExceptionDTO apiExceptionDTO = response.getBody();

        Assertions.assertEquals(403, (int) apiExceptionDTO.getStatus());
        Assertions.assertEquals(RATE_LIMIT_EXCEEDED_MSG, apiExceptionDTO.getMessage());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
