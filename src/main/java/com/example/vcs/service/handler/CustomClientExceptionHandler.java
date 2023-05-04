package com.example.vcs.service.handler;

import com.example.vcs.service.exception.github.GithubApiRateLimitExceededException;
import com.example.vcs.service.exception.github.GithubResourceNotFoundException;
import com.example.vcs.constants.EndpointsConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;

public class CustomClientExceptionHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if(response.getStatusCode().value() == HttpStatus.NOT_FOUND.value()){
            throw new GithubResourceNotFoundException(EndpointsConstants.ErrorMessages.RESOURCE_NOT_FOUND_MSG);
        }else if(response.getStatusCode().value() == HttpStatus.FORBIDDEN.value()){
            throw new GithubApiRateLimitExceededException(EndpointsConstants.ErrorMessages.RATE_LIMIT_EXCEEDED_MSG);
        }
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ResponseErrorHandler.super.handleError(url, method, response);
    }
}
