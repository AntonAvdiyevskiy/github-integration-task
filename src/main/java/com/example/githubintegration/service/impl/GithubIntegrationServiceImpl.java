package com.example.githubintegration.service.impl;

import com.example.githubintegration.dto.BranchDTO;
import com.example.githubintegration.dto.RepositoryDTO;
import com.example.githubintegration.dto.UserRepositoriesDetailsDTO;
import com.example.githubintegration.integration.GithubApiClient;
import com.example.githubintegration.service.GithubIntegrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GithubIntegrationServiceImpl implements GithubIntegrationService {
    private GithubApiClient githubApiClient;

    private ModelMapper modelMapper;

    @Autowired
    public GithubIntegrationServiceImpl(GithubApiClient githubApiClient,
                                        ModelMapper modelMapper) {
        this.githubApiClient = githubApiClient;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserRepositoriesDetailsDTO> getUsersRepositories(String username, int sizeOfPage, int numberOfPage) {

        List<RepositoryDTO> repositories = List.of(githubApiClient
                .getUserRepositories(username, sizeOfPage, numberOfPage));

        return filterNotForkRepositories(repositories)
                .stream()
                .map(generateUserRepositoriesDetailsDTO(sizeOfPage, numberOfPage))
                .collect(Collectors.toList());
    }

    private List<RepositoryDTO> filterNotForkRepositories(List<RepositoryDTO> repositoryDTOS) {
        return repositoryDTOS.stream()
                .filter(repositoryDTO -> !repositoryDTO.isFork())
                .collect(Collectors.toList());
    }

    private Function<RepositoryDTO, UserRepositoriesDetailsDTO> generateUserRepositoriesDetailsDTO(
            int sizeOfPage,
            int numberOfPage
    ) {
        return repositoryDTO -> {
            List<BranchDTO> branchDTOS = List.of(githubApiClient.getRepositoryBranches(repositoryDTO.getOwner().getLogin(),
                    repositoryDTO.getName(),
                    sizeOfPage,
                    numberOfPage));
            UserRepositoriesDetailsDTO userRepositoriesDetailsDTO = modelMapper.map(repositoryDTO, UserRepositoriesDetailsDTO.class);
            userRepositoriesDetailsDTO.setBranches(branchDTOS);


            return userRepositoriesDetailsDTO;
        };
    }


}
