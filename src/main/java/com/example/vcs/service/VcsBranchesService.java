package com.example.vcs.service;

import java.util.List;

public interface VcsBranchesService<T> {
    List<T> getBranches(String username, String repositoryName, int sizeOfPage,
                        int numberOfPage);
}
