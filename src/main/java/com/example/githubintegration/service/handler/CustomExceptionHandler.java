package com.example.githubintegration.service.handler;

import com.example.githubintegration.service.exception.ApiExceptionDTO;
import com.example.githubintegration.service.exception.GithubResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    public ResponseEntity<ApiExceptionDTO> handleValidationExceptions(HttpMediaTypeNotAcceptableException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON).
                body(buildException(HttpStatus.NOT_ACCEPTABLE, errorMessage));
    }

    @ExceptionHandler(GithubResourceNotFoundException.class)
    public ResponseEntity<ApiExceptionDTO> handleNotFoundExceptions(GithubResourceNotFoundException ex) {
        String errorMessage = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildException(HttpStatus.NOT_FOUND, errorMessage));
    }

    private ApiExceptionDTO buildException(HttpStatus status, String errorMessage) {
        return new ApiExceptionDTO(status.value(), errorMessage);
    }
}
