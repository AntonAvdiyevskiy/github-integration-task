package com.example.githubintegration.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GithubRepositoriesListDTO {
    private List<RepositoryDTO> repositories;
}
