package com.example.githubintegration.integration.impl;

import com.example.githubintegration.dto.BranchDTO;
import com.example.githubintegration.dto.GithubRepositoriesListDTO;
import com.example.githubintegration.dto.RepositoryDTO;
import com.example.githubintegration.integration.GithubApiClient;
import com.example.githubintegration.integration.GithubResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.githubintegration.constants.GithubEndpointsConstants.UserEndpoints.USER_REPOS_ENDPOINT;
import static com.example.githubintegration.constants.GithubEndpointsConstants.ReposEndpoints.BRANCHES_ENDPOINT;

@Service
public class GithubApiClientImpl implements GithubApiClient {
    private GithubResources githubResources;

    @Autowired
    public GithubApiClientImpl(GithubResources githubResources) {
        this.githubResources = githubResources;
    }

    @Override
    public RepositoryDTO[] getUserRepositories(String username,
                                                         int sizeOfPage,
                                                         int numberOfPage) {
        ResponseEntity<RepositoryDTO[]> response = githubResources
                .get(String.format(USER_REPOS_ENDPOINT, username),
                        RepositoryDTO[].class,
                        generatePageRequestParams(sizeOfPage, numberOfPage));
        return response.getBody();
    }

    @Override
    public BranchDTO[] getRepositoryBranches(String username,
                                             String repositoryName,
                                                 int sizeOfPage,
                                                 int numberOfPage) {
        ResponseEntity<BranchDTO[]> response = githubResources
                .get(String.format(BRANCHES_ENDPOINT, username, repositoryName),
                        BranchDTO[].class,
                        generatePageRequestParams(sizeOfPage, numberOfPage));
        return response.getBody();
    }

    private Map<String, Integer> generatePageRequestParams(int sizeOfPage, int numberOfPage){
        return Map.of("per_page", sizeOfPage,
                "page", numberOfPage);
    }
}
