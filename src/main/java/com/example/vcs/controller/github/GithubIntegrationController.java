package com.example.vcs.controller.github;

import com.example.vcs.dto.UserRepositoriesDetailsDTO;
import com.example.vcs.service.VcsRepositoriesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;


@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GithubIntegrationController {
    private final VcsRepositoriesService<UserRepositoriesDetailsDTO> vcsRepositoriesService;

    @GetMapping(value = "/user/{username}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get GitHub Repos of user"
    )
    public List<UserRepositoriesDetailsDTO> getUserGitHubData(@PathVariable String username,
                                                                @RequestParam(name ="per_page", required = false, defaultValue = "30") Integer sizeOfPage,
                                                                @RequestParam(name = "page", required = false, defaultValue = "1") Integer numberOfPage) {
        return vcsRepositoriesService.getUsersRepositories(username,
                sizeOfPage,
                numberOfPage);
    }
}
