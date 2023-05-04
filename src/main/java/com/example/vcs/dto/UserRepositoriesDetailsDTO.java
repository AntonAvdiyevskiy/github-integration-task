package com.example.vcs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRepositoriesDetailsDTO {
    private String name;
    private String ownerName;
    private List<BranchDTO> branches;
}
