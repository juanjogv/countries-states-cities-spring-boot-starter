package com.csc.service;

import com.csc.repository.SubregionRepository;

public class SubregionService {

    private final SubregionRepository subregionRepository;

    public SubregionService(SubregionRepository subregionRepository) {
        this.subregionRepository = subregionRepository;
    }
}
