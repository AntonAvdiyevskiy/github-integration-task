package com.example.githubintegration.service.handler;

import com.example.githubintegration.service.exception.GithubResourceNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

import static com.example.githubintegration.constants.GithubEndpointsConstants.ErrorMessages.RESOURCE_NOT_FOUND_MSG;

public class CustomGithubExceptionHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if(response.getStatusCode().value() == HttpStatus.NOT_FOUND.value()){
            throw new GithubResourceNotFoundException(RESOURCE_NOT_FOUND_MSG);
        }
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ResponseErrorHandler.super.handleError(url, method, response);
    }
}
