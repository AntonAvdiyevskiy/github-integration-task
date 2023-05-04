package com.example.vcs.service;

import java.util.List;

public interface VcsRepositoriesService <T> {

    List<T> getUsersRepositories(String username, int sizeOfPage,
                                                          int numberOfPage);
}
