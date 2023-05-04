package com.example.vcs.integration;

import java.util.List;

public interface VCSBranchesClient<T> {
    List<T> getBranches(String username, String repositoryName, int sizeOfPage, int numberOfPage);
}
