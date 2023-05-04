package com.example.vcs.service.impl.github;

import com.example.vcs.dto.BranchDTO;
import com.example.vcs.dto.OwnerDTO;
import com.example.vcs.dto.RepositoryDTO;
import com.example.vcs.dto.UserRepositoriesDetailsDTO;
import com.example.vcs.integration.VCSRepositoriesClient;
import com.example.vcs.integration.impl.github.GithubRepositoriesClientImpl;
import com.example.vcs.service.VcsBranchesService;
import com.example.vcs.service.VcsRepositoriesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.mockito.Mockito.when;

public class GithubRepositoriesServiceImplTest {

    private VcsBranchesService<BranchDTO> vcsBranchesClient =
            Mockito.mock(GithubBranchesServiceImpl.class);

    private VCSRepositoriesClient<RepositoryDTO> vcsRepositoriesClient =
            Mockito.mock(GithubRepositoriesClientImpl.class);

    private ModelMapper modelMapper = Mockito.mock(ModelMapper.class);

    private VcsRepositoriesService<UserRepositoriesDetailsDTO> vcsIntegrationService;

    private static final String REPO_NAME1 = "some1";
    private static final String REPO_NAME2 = "some2";
    private static final String OWNER_NAME = "Anton";
    private static final String USERNAME = "AntonAVD";

    private static final String BRANCH_NAME1 = "master";
    private static final String BRANCH_NAME2 = "stage";

    @BeforeEach
    void init_mocks() {
        vcsIntegrationService = new GithubRepositoriesServiceImpl(modelMapper, vcsBranchesClient, vcsRepositoriesClient);
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

        List<RepositoryDTO> repositoryDTOS = List.of(repositoryDTO1, repositoryDTO2);
        List<BranchDTO> branchDTOS1 = List.of(branchDTO1);
        List<BranchDTO> branchDTOS2 = List.of(branchDTO2);

        when(vcsRepositoriesClient
                .getRepositories(USERNAME, 30, 1))
                .thenReturn(repositoryDTOS);
        when(vcsBranchesClient
                .getBranches(repositoryDTO1.getOwner().getLogin(),
                        repositoryDTO1.getName(), 30, 1))
                .thenReturn(branchDTOS1);
        when(vcsBranchesClient
                .getBranches(repositoryDTO2.getOwner().getLogin(),
                        repositoryDTO2.getName(), 30, 1))
                .thenReturn(branchDTOS2);
        when(modelMapper.map(repositoryDTO1, UserRepositoriesDetailsDTO.class))
                .thenReturn(userRepositoriesDetailsDTO1);
        when(modelMapper.map(repositoryDTO2, UserRepositoriesDetailsDTO.class))
                .thenReturn(userRepositoriesDetailsDTO2);

        List<UserRepositoriesDetailsDTO> result = vcsIntegrationService.getUsersRepositories(USERNAME, 30, 1);

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

        List<RepositoryDTO> repositoryDTOS = List.of(repositoryDTO1, repositoryDTO2);


        when(vcsRepositoriesClient
                .getRepositories(USERNAME, 30, 1))
                .thenReturn(repositoryDTOS);

        List<UserRepositoriesDetailsDTO> result = vcsIntegrationService.getUsersRepositories(USERNAME, 30, 1);

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

        List<RepositoryDTO> repositoryDTOS = List.of(repositoryDTO1, repositoryDTO2);
        List<BranchDTO> branchDTOS1 = List.of(branchDTO1);

        when(vcsRepositoriesClient
                .getRepositories(USERNAME, 30, 1))
                .thenReturn(repositoryDTOS);
        when(vcsBranchesClient
                .getBranches(repositoryDTO1.getOwner().getLogin(),
                        repositoryDTO1.getName(), 30, 1))
                .thenReturn(branchDTOS1);

        when(modelMapper.map(repositoryDTO1, UserRepositoriesDetailsDTO.class))
                .thenReturn(userRepositoriesDetailsDTO1);

        List<UserRepositoriesDetailsDTO> result = vcsIntegrationService.getUsersRepositories(USERNAME, 30, 1);

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
