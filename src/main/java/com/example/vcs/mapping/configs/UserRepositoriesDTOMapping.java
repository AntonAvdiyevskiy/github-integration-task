package com.example.vcs.mapping.configs;

import com.example.vcs.dto.RepositoryDTO;
import com.example.vcs.dto.UserRepositoriesDetailsDTO;
import com.example.vcs.mapping.MappingConfig;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoriesDTOMapping implements MappingConfig {

    @Override
    public void apply(ModelMapper mapper) {
        configureRepositoryDTOToUserRepositoriesDetailsDTOMapping(mapper);
    }

    private void configureRepositoryDTOToUserRepositoriesDetailsDTOMapping(ModelMapper mapper){
        mapper.typeMap(RepositoryDTO.class, UserRepositoriesDetailsDTO.class)
                .addMapping(src -> src.getOwner().getLogin(), UserRepositoriesDetailsDTO::setOwnerName);
    }
}
