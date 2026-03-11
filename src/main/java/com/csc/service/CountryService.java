package com.csc.service;

import com.csc.model.Country;
import com.csc.repository.CountryRepository;
import java.util.List;
import java.util.Optional;

public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> findAllCountries() {
        return countryRepository.findAll();
    }

    public Optional<Country> findCountryByCode(String iso2Code) {
        return countryRepository.findByIso2(iso2Code);
    }

    public List<Country> findAllCountries(List<String> fields) {
        return countryRepository.findAll(fields);
    }

    public Optional<Country> findCountryByCode(String iso2Code, List<String> fields) {
        return countryRepository.findByIso2(iso2Code, fields);
    }
}
