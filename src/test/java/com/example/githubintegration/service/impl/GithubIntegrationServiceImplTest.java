package com.example.githubintegration.service.impl;

import com.example.githubintegration.dto.BranchDTO;
import com.example.githubintegration.dto.OwnerDTO;
import com.example.githubintegration.dto.RepositoryDTO;
import com.example.githubintegration.dto.UserRepositoriesDetailsDTO;
import com.example.githubintegration.integration.GithubApiClient;
import com.example.githubintegration.service.GithubIntegrationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = GithubIntegrationServiceImpl.class)
public class GithubIntegrationServiceImplTest {
    @MockBean
    private GithubApiClient githubApiClient;

    @MockBean
    private ModelMapper modelMapper;

    private GithubIntegrationService githubIntegrationService;

    private static final String REPO_NAME1 = "some1";
    private static final String REPO_NAME2 = "some2";
    private static final String OWNER_NAME = "Anton";
    private static final String USERNAME = "AntonAVD";

    private static final String BRANCH_NAME1 = "master";
    private static final String BRANCH_NAME2 = "stage";

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
        githubIntegrationService = new GithubIntegrationServiceImpl(githubApiClient, modelMapper);
    }

    @Test
    public void getUserRepositories_repositoriesPresent() {
        RepositoryDTO repositoryDTO1 = prepareUserRepositoryDTO(REPO_NAME1, false);
        RepositoryDTO repositoryDTO2 = prepareUserRepositoryDTO(REPO_NAME2, false);
        UserRepositoriesDetailsDTO userRepositoriesDetailsDTO1 = new UserRepositoriesDetailsDTO();
        userRepositoriesDetailsDTO1.setName(REPO_NAME1);
        userRepositoriesDetailsDTO1.setOwnerName(OWNER_NAME);
        UserRepositoriesDetailsDTO userRepositoriesDetailsDTO2 = new UserRepositoriesDetailsDTO();
        userRepositoriesDetailsDTO2.setName(REPO_NAME2);
        userRepositoriesDetailsDTO2.setOwnerName(OWNER_NAME);
        BranchDTO branchDTO1 = prepareBranchDTO(BRANCH_NAME1);
        BranchDTO branchDTO2 = prepareBranchDTO(BRANCH_NAME2);

        RepositoryDTO[] repositoryDTOS = new RepositoryDTO[]{repositoryDTO1, repositoryDTO2};
        BranchDTO[] branchDTOS1 = new BranchDTO[]{branchDTO1};
        BranchDTO[] branchDTOS2 = new BranchDTO[]{branchDTO2};

        when(githubApiClient
                .getUserRepositories(USERNAME, 30, 1))
                .thenReturn(repositoryDTOS);
        when(githubApiClient
                .getRepositoryBranches(repositoryDTO1.getOwner().getLogin(),
                        repositoryDTO1.getName(), 30, 1))
                .thenReturn(branchDTOS1);
        when(githubApiClient
                .getRepositoryBranches(repositoryDTO2.getOwner().getLogin(),
                        repositoryDTO2.getName(), 30, 1))
                .thenReturn(branchDTOS2);
        when(modelMapper.map(repositoryDTO1, UserRepositoriesDetailsDTO.class))
                .thenReturn(userRepositoriesDetailsDTO1);
        when(modelMapper.map(repositoryDTO2, UserRepositoriesDetailsDTO.class))
                .thenReturn(userRepositoriesDetailsDTO2);

        List<UserRepositoriesDetailsDTO> result = githubIntegrationService.getUsersRepositories(USERNAME, 30, 1);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(OWNER_NAME, result.get(0).getOwnerName());
        Assertions.assertEquals(OWNER_NAME, result.get(1).getOwnerName());
        Assertions.assertEquals(REPO_NAME1, result.get(0).getName());
        Assertions.assertEquals(REPO_NAME2, result.get(1).getName());
        Assertions.assertEquals(BRANCH_NAME1, result.get(0).getBranches().get(0).getName());
        Assertions.assertEquals(BRANCH_NAME2, result.get(1).getBranches().get(0).getName());
    }

    @Test
    public void getUserRepositories_AllReposAreForks() {
        RepositoryDTO repositoryDTO1 = prepareUserRepositoryDTO(REPO_NAME1, true);
        RepositoryDTO repositoryDTO2 = prepareUserRepositoryDTO(REPO_NAME2, true);
        UserRepositoriesDetailsDTO userRepositoriesDetailsDTO1 = new UserRepositoriesDetailsDTO();
        userRepositoriesDetailsDTO1.setName(REPO_NAME1);
        userRepositoriesDetailsDTO1.setOwnerName(OWNER_NAME);
        UserRepositoriesDetailsDTO userRepositoriesDetailsDTO2 = new UserRepositoriesDetailsDTO();
        userRepositoriesDetailsDTO2.setName(REPO_NAME2);
        userRepositoriesDetailsDTO2.setOwnerName(OWNER_NAME);

        RepositoryDTO[] repositoryDTOS = new RepositoryDTO[]{repositoryDTO1, repositoryDTO2};


        when(githubApiClient
                .getUserRepositories(USERNAME, 30, 1))
                .thenReturn(repositoryDTOS);

        List<UserRepositoriesDetailsDTO> result = githubIntegrationService.getUsersRepositories(USERNAME, 30, 1);

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void getUserRepositories_OneRepoIsFork() {
        RepositoryDTO repositoryDTO1 = prepareUserRepositoryDTO(REPO_NAME1, false);
        RepositoryDTO repositoryDTO2 = prepareUserRepositoryDTO(REPO_NAME2, true);
        UserRepositoriesDetailsDTO userRepositoriesDetailsDTO1 = new UserRepositoriesDetailsDTO();
        userRepositoriesDetailsDTO1.setName(REPO_NAME1);
        userRepositoriesDetailsDTO1.setOwnerName(OWNER_NAME);
        UserRepositoriesDetailsDTO userRepositoriesDetailsDTO2 = new UserRepositoriesDetailsDTO();
        userRepositoriesDetailsDTO2.setName(REPO_NAME2);
        userRepositoriesDetailsDTO2.setOwnerName(OWNER_NAME);
        BranchDTO branchDTO1 = prepareBranchDTO(BRANCH_NAME1);

        RepositoryDTO[] repositoryDTOS = new RepositoryDTO[]{repositoryDTO1, repositoryDTO2};
        BranchDTO[] branchDTOS1 = new BranchDTO[]{branchDTO1};

        when(githubApiClient
                .getUserRepositories(USERNAME, 30, 1))
                .thenReturn(repositoryDTOS);
        when(githubApiClient
                .getRepositoryBranches(repositoryDTO1.getOwner().getLogin(),
                        repositoryDTO1.getName(), 30, 1))
                .thenReturn(branchDTOS1);

        when(modelMapper.map(repositoryDTO1, UserRepositoriesDetailsDTO.class))
                .thenReturn(userRepositoriesDetailsDTO1);

        List<UserRepositoriesDetailsDTO> result = githubIntegrationService.getUsersRepositories(USERNAME, 30, 1);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(REPO_NAME1, result.get(0).getName());
        Assertions.assertEquals(BRANCH_NAME1, result.get(0).getBranches().get(0).getName());
    }

    private RepositoryDTO prepareUserRepositoryDTO(String name, boolean isFork) {
        RepositoryDTO repositoryDTO = new RepositoryDTO();
        repositoryDTO.setFork(isFork);
        repositoryDTO.setName(name);
        repositoryDTO.setOwner(prepareOwnerDTO());

        return repositoryDTO;
    }

    private OwnerDTO prepareOwnerDTO() {
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setLogin(OWNER_NAME);

        return ownerDTO;
    }

    private BranchDTO prepareBranchDTO(String name) {
        BranchDTO branchDTO = new BranchDTO();
        branchDTO.setName(name);

        return branchDTO;
    }


}
