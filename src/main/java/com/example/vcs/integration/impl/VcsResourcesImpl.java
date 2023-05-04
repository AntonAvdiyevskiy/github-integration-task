package com.example.vcs.integration.impl;

import com.example.vcs.integration.VcsResources;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class VcsResourcesImpl implements VcsResources {

    private final RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> params) {
        return doRequest(url, HttpMethod.GET, new HttpEntity<>(generateHeaders()), responseType, params);
    }

    private <T> ResponseEntity<T> doRequest(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseClass, Map<String, ?> params) {

        return restTemplate.exchange(url, method, requestEntity, responseClass, params);
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
