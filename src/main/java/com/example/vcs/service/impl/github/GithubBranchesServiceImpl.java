package com.example.vcs.service.impl.github;

import com.example.vcs.dto.BranchDTO;
import com.example.vcs.integration.VCSBranchesClient;
import com.example.vcs.service.VcsBranchesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GithubBranchesServiceImpl implements VcsBranchesService<BranchDTO> {
    private VCSBranchesClient<BranchDTO> vcsBranchesClient;

    @Override
    public List<BranchDTO> getBranches(String username, String repositoryName,
                                       int sizeOfPage, int numberOfPage) {
        return vcsBranchesClient.getBranches(username,
                repositoryName,
                sizeOfPage,
                numberOfPage);
    }
}
