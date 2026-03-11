package com.csc.service;

import com.csc.model.State;
import com.csc.repository.StateRepository;
import java.util.List;
import java.util.Optional;

public class StateService {

    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public Optional<State> findStateByCountryCodeAndStateCode(String countryIso2Code, String stateIso2Code) {
        return stateRepository.findByCountryCodeAndIso2(countryIso2Code, stateIso2Code);
    }

    public List<State> findStatesByCountryCode(String countryIso2Code) {
        return stateRepository.findByCountryCode(countryIso2Code);
    }

    public Optional<State> findStateByCountryCodeAndStateCode(
            String countryIso2Code, String stateIso2Code, List<String> fields) {
        return stateRepository.findByCountryCodeAndIso2(countryIso2Code, stateIso2Code, fields);
    }

    public List<State> findStatesByCountryCode(String countryIso2Code, List<String> fields) {
        return stateRepository.findByCountryCode(countryIso2Code, fields);
    }
}
