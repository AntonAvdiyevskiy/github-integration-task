package com.example.githubintegration.controller;

import com.example.githubintegration.service.GithubIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/github")
public class GithubIntegrationController {
    private GithubIntegrationService githubIntegrationService;

    @Autowired
    public GithubIntegrationController(GithubIntegrationService githubIntegrationService) {
        this.githubIntegrationService = githubIntegrationService;
    }

    @GetMapping(value = "/user/{username}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get GitHub Repos of user"
    )
    public ResponseEntity<?> startMeetingsImport(@PathVariable String username,
                                                 @RequestParam(name ="per_page", required = false, defaultValue = "30") Integer sizeOfPage,
                                                 @RequestParam(name = "page", required = false, defaultValue = "1") Integer numberOfPage) {
        return ResponseEntity.ok(githubIntegrationService.getUsersRepositories(username,
                sizeOfPage,
                numberOfPage));
    }
}
