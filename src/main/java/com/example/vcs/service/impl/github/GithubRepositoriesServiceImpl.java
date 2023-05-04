package com.example.vcs.service.impl.github;

import com.example.vcs.dto.BranchDTO;
import com.example.vcs.dto.RepositoryDTO;
import com.example.vcs.dto.UserRepositoriesDetailsDTO;
import com.example.vcs.integration.VCSRepositoriesClient;
import com.example.vcs.service.VcsBranchesService;
import com.example.vcs.service.VcsRepositoriesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GithubRepositoriesServiceImpl implements VcsRepositoriesService<UserRepositoriesDetailsDTO> {

    @Value("${vcs.repositories.fork}")
    private boolean isFork;

    private final ModelMapper modelMapper;

    private final VcsBranchesService<BranchDTO> vcsBranchesService;

    private final VCSRepositoriesClient<RepositoryDTO> vcsRepositoriesClient;

    @Autowired
    public GithubRepositoriesServiceImpl(ModelMapper modelMapper,
                                         VcsBranchesService<BranchDTO> vcsBranchesService,
                                         VCSRepositoriesClient<RepositoryDTO> vcsRepositoriesClient) {
        this.modelMapper = modelMapper;
        this.vcsBranchesService = vcsBranchesService;
        this.vcsRepositoriesClient = vcsRepositoriesClient;
    }

    @Override
    public List<UserRepositoriesDetailsDTO> getUsersRepositories(String username, int sizeOfPage, int numberOfPage) {

        List<RepositoryDTO> repositories = vcsRepositoriesClient
                .getRepositories(username, sizeOfPage, numberOfPage);

        return filterNotForkRepositories(repositories)
                .stream()
                .map(generateUserRepositoriesDetailsDTO(sizeOfPage, numberOfPage))
                .collect(Collectors.toList());
    }

    private List<RepositoryDTO> filterNotForkRepositories(List<RepositoryDTO> repositoryDTOS) {
        return repositoryDTOS.stream()
                .filter(repositoryDTO -> repositoryDTO.isFork() == isFork)
                .collect(Collectors.toList());
    }

    private Function<RepositoryDTO, UserRepositoriesDetailsDTO> generateUserRepositoriesDetailsDTO(
            int sizeOfPage,
            int numberOfPage
    ) {
        return repositoryDTO -> {
            List<BranchDTO> branchDTOS = vcsBranchesService.getBranches(repositoryDTO.getOwner().getLogin(),
                    repositoryDTO.getName(),
                    sizeOfPage,
                    numberOfPage);
            UserRepositoriesDetailsDTO userRepositoriesDetailsDTO = modelMapper.map(repositoryDTO, UserRepositoriesDetailsDTO.class);
            userRepositoriesDetailsDTO.setBranches(branchDTOS);


            return userRepositoriesDetailsDTO;
        };
    }

}
