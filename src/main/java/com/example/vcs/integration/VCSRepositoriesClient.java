package com.example.vcs.integration;

import java.util.List;

public interface VCSRepositoriesClient<T> {

    List<T> getRepositories(String username, int sizeOfPage, int numberOfPage);
}
