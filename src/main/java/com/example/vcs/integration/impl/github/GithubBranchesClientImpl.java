package com.example.vcs.integration.impl.github;

import com.example.vcs.config.github.GithubEndpointsValues;
import com.example.vcs.dto.BranchDTO;
import com.example.vcs.integration.VcsResources;
import com.example.vcs.integration.VCSBranchesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubBranchesClientImpl implements VCSBranchesClient<BranchDTO> {

    private final VcsResources vcsResources;

    private final GithubEndpointsValues githubEndpointsValues;

    @Override
    public List<BranchDTO> getBranches(String username,
                                                 String repositoryName,
                                                 int sizeOfPage,
                                                 int numberOfPage) {
        BranchDTO[] branchDTOS = vcsResources
                .get(String.format(githubEndpointsValues.getGithubBranchesEndpoint(), username, repositoryName),
                        BranchDTO[].class,
                        Map.of("per_page", sizeOfPage,
                                "page", numberOfPage)).getBody();

        return List.of(branchDTOS);
    }
}
