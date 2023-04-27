package com.example.githubintegration.integration;

import com.example.githubintegration.dto.BranchDTO;
import com.example.githubintegration.dto.GithubRepositoriesListDTO;
import com.example.githubintegration.dto.RepositoryDTO;

import java.util.List;

public interface GithubApiClient {
    RepositoryDTO[] getUserRepositories(String username, int sizeOfPage,
                                        int numberOfPage);

    BranchDTO[] getRepositoryBranches(String username,
                                      String repositoryName,
                                      int sizeOfPage,
                                      int numberOfPage);
}
