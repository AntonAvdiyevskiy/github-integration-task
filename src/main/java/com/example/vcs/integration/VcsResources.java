package com.example.vcs.integration;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface VcsResources {
    <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> params);
}
