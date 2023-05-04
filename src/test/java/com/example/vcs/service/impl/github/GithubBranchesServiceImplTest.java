package com.example.vcs.service.impl.github;

import com.example.vcs.dto.BranchDTO;
import com.example.vcs.integration.VCSBranchesClient;
import com.example.vcs.integration.impl.github.GithubBranchesClientImpl;
import com.example.vcs.service.VcsBranchesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GithubBranchesServiceImplTest {

    private VCSBranchesClient<BranchDTO> vcsBranchesClient =
            Mockito.mock(GithubBranchesClientImpl.class);

    private VcsBranchesService<BranchDTO> vcsBranchesService;

    private static final String REPO_NAME = "some1";

    private static final String USERNAME = "AntonAVD";

    @BeforeEach
    void init_mocks() {
        vcsBranchesService = new GithubBranchesServiceImpl(vcsBranchesClient);
    }

    @Test
    public void getBranches_VerifyClientInvocation(){
        vcsBranchesService.getBranches(USERNAME, REPO_NAME, 1, 1);

        Mockito.verify(vcsBranchesClient).getBranches(USERNAME, REPO_NAME, 1, 1);
    }
}
