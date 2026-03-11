package com.csc.service;

import com.csc.repository.RegionRepository;

public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }
}
