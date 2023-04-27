package com.example.githubintegration.service.exception;

public class GithubResourceNotFoundException extends RuntimeException {
    public GithubResourceNotFoundException(String message) {
        super(message);
    }
}
