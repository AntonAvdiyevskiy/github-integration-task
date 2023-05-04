package com.example.vcs.config.github;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GithubEndpointsValues {

    @Value("${github.branches.endpoint}")
    private String githubBranchesEndpoint;

    @Value("${github.user.repos.endpoint}")
    private String githubUserReposEndpoint;
}
