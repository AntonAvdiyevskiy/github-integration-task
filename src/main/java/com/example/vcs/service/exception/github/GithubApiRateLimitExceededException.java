package com.example.vcs.service.exception.github;

public class GithubApiRateLimitExceededException extends RuntimeException {

    public GithubApiRateLimitExceededException(String message) {
        super(message);
    }
}
