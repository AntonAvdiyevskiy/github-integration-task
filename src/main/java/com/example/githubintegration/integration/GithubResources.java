package com.example.githubintegration.integration;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GithubResources {
    <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> params);
}
