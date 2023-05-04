package com.example.vcs.integration.impl.github;

import com.example.vcs.config.github.GithubEndpointsValues;
import com.example.vcs.dto.RepositoryDTO;
import com.example.vcs.integration.VcsResources;
import com.example.vcs.integration.VCSRepositoriesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubRepositoriesClientImpl implements VCSRepositoriesClient<RepositoryDTO> {

    private final VcsResources vcsResources;

    private final GithubEndpointsValues githubEndpointsValues;

    @Override
    public List<RepositoryDTO> getRepositories(String username, int sizeOfPage, int numberOfPage) {
        RepositoryDTO[] repos = vcsResources
                .get(String.format(githubEndpointsValues.getGithubUserReposEndpoint(), username),
                        RepositoryDTO[].class,
                        Map.of("per_page", sizeOfPage,
                                "page", numberOfPage)).getBody();

        return List.of(repos);
    }

}
