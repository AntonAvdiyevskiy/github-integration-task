package com.example.vcs.service.exception.github;

public class GithubResourceNotFoundException extends RuntimeException {

    public GithubResourceNotFoundException(String message) {
        super(message);
    }

}
