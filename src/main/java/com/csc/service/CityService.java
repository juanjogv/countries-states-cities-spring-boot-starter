package com.csc.service;

import com.csc.model.City;
import com.csc.repository.CityRepository;
import java.util.List;

public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> findCitiesByCountryCodeAndStateCode(String countryIso2Code, String stateIso2Code) {
        return cityRepository.findByCountryCodeAndStateCode(countryIso2Code, stateIso2Code);
    }

    public List<City> findCitiesByCountryCode(String countryIso2Code) {
        return cityRepository.findByCountryCode(countryIso2Code);
    }

    public List<City> findCitiesByCountryCodeAndStateCode(
            String countryIso2Code, String stateIso2Code, List<String> fields) {
        return cityRepository.findByCountryCodeAndStateCode(countryIso2Code, stateIso2Code, fields);
    }

    public List<City> findCitiesByCountryCode(String countryIso2Code, List<String> fields) {
        return cityRepository.findByCountryCode(countryIso2Code, fields);
    }
}
