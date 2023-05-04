package com.example.vcs.mapping;

import org.modelmapper.ModelMapper;

public interface MappingConfig {
    void apply(ModelMapper mapper);
}
