package com.example.vcs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepositoryDTO {
    private String name;

    private boolean fork;

    private OwnerDTO owner;
}
