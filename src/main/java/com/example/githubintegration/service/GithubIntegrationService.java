package com.example.githubintegration.service;

import com.example.githubintegration.dto.UserRepositoriesDetailsDTO;

import java.util.List;

public interface GithubIntegrationService {
    List<UserRepositoriesDetailsDTO> getUsersRepositories(String username, int sizeOfPage,
                                                          int numberOfPage);
}
