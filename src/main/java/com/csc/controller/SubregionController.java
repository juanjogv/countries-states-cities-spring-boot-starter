package com.csc.controller;

import com.csc.service.SubregionService;

public class SubregionController {

    private final SubregionService subregionService;

    public SubregionController(SubregionService subregionService) {
        this.subregionService = subregionService;
    }
}
