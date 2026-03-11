package com.csc.controller;

import com.csc.service.RegionService;

public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }
}
