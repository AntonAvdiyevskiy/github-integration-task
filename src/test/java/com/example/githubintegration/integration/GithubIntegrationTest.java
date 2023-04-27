package com.example.githubintegration.integration;

import com.example.githubintegration.dto.UserRepositoriesDetailsDTO;
import com.example.githubintegration.service.exception.ApiExceptionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static com.example.githubintegration.constants.GithubEndpointsConstants.ErrorMessages.RESOURCE_NOT_FOUND_MSG;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubIntegrationTest {
    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private static HttpHeaders httpHeaders;

    private static final String ACCEPT_TYPE_HEADER = "Accept";
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_XML = "application/xml";
    private static final String NOT_SUPPORTED_TYPE = "No acceptable representation";

    @BeforeEach
    public void initializeTestData() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, APPLICATION_JSON);
    }

    @Test
    public void getRepositories_successfulResponse() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<UserRepositoriesDetailsDTO[]> response = restTemplate.exchange(
                createURLWithPort("/github/user/AntonAvdiyevskiy/repos"), HttpMethod.GET, entity, UserRepositoriesDetailsDTO[].class);

        List<UserRepositoriesDetailsDTO> userRepositoriesDetailsDTOS = Arrays.asList(response.getBody());

        Assertions.assertFalse(userRepositoriesDetailsDTOS.isEmpty());
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

        ResponseEntity<ApiExceptionDTO> response = restTemplate.exchange(
                createURLWithPort("/github/user/AntonAvdiyevskiy1/repos"), HttpMethod.GET, entity, ApiExceptionDTO.class);
        ApiExceptionDTO apiExceptionDTO = response.getBody();

        Assertions.assertEquals(404, (int) apiExceptionDTO.getStatus());
        Assertions.assertEquals(RESOURCE_NOT_FOUND_MSG, apiExceptionDTO.getMessage());
        Assertions.assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    public void getRepositories_PaginationPageSizeSucceeded() {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<UserRepositoriesDetailsDTO[]> response = restTemplate.exchange(
                createURLWithPort("/github/user/AntonAvdiyevskiy/repos?per_page=1"), HttpMethod.GET, entity, UserRepositoriesDetailsDTO[].class);
        List<UserRepositoriesDetailsDTO> userRepositoriesDetailsDTOS = Arrays.asList(response.getBody());

        Assertions.assertEquals(1, userRepositoriesDetailsDTOS.size());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
