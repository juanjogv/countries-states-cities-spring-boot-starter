package com.csc.autoconfigure;

import com.csc.config.CscDatasource;
import com.csc.config.CscProperties;
import com.csc.controller.CityController;
import com.csc.controller.CountryController;
import com.csc.controller.RegionController;
import com.csc.controller.StateController;
import com.csc.controller.SubregionController;
import com.csc.repository.CityRepository;
import com.csc.repository.CountryRepository;
import com.csc.repository.RegionRepository;
import com.csc.repository.StateRepository;
import com.csc.repository.SubregionRepository;
import com.csc.service.CityService;
import com.csc.service.CountryService;
import com.csc.service.RegionService;
import com.csc.service.StateService;
import com.csc.service.SubregionService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Auto-configuration for the Countries-States-Cities library.
 * <p>
 * Activates when the property {@code csc.enabled} is {@code true} (default).
 * Registers its own {@link javax.sql.DataSource} and {@link JdbcTemplate} qualified
 * with {@link CscDatasource}, so the user's primary data source is never touched.
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "csc", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CscProperties.class)
@Import({CscDataSourceConfiguration.class})
public class CscAutoConfiguration {

    // ── Repositories ────────────────────────────────────────────────

    @Bean
    public RegionRepository regionRepository(@CscDatasource JdbcTemplate jdbcTemplate) {
        return new RegionRepository(jdbcTemplate);
    }

    @Bean
    public SubregionRepository subregionRepository(@CscDatasource JdbcTemplate jdbcTemplate) {
        return new SubregionRepository(jdbcTemplate);
    }

    @Bean
    public CountryRepository countryRepository(@CscDatasource JdbcTemplate jdbcTemplate) {
        return new CountryRepository(jdbcTemplate);
    }

    @Bean
    public StateRepository stateRepository(@CscDatasource JdbcTemplate jdbcTemplate) {
        return new StateRepository(jdbcTemplate);
    }

    @Bean
    public CityRepository cityRepository(@CscDatasource JdbcTemplate jdbcTemplate) {
        return new CityRepository(jdbcTemplate);
    }

    // ── Services ────────────────────────────────────────────────────

    @Bean
    public RegionService regionService(RegionRepository regionRepository) {
        return new RegionService(regionRepository);
    }

    @Bean
    public SubregionService subregionService(SubregionRepository subregionRepository) {
        return new SubregionService(subregionRepository);
    }

    @Bean
    public CountryService countryService(CountryRepository countryRepository) {
        return new CountryService(countryRepository);
    }

    @Bean
    public StateService stateService(StateRepository stateRepository) {
        return new StateService(stateRepository);
    }

    @Bean
    public CityService cityService(CityRepository cityRepository) {
        return new CityService(cityRepository);
    }

    // ── Controllers ─────────────────────────────────────────────────

    @Bean
    @ConditionalOnProperty(prefix = "csc", name = "expose-api", havingValue = "true", matchIfMissing = true)
    public RegionController regionController(RegionService regionService) {
        return new RegionController(regionService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "csc", name = "expose-api", havingValue = "true", matchIfMissing = true)
    public SubregionController subregionController(SubregionService subregionService) {
        return new SubregionController(subregionService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "csc", name = "expose-api", havingValue = "true", matchIfMissing = true)
    public CountryController countryController(CountryService countryService) {
        return new CountryController(countryService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "csc", name = "expose-api", havingValue = "true", matchIfMissing = true)
    public StateController stateController(StateService stateService) {
        return new StateController(stateService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "csc", name = "expose-api", havingValue = "true", matchIfMissing = true)
    public CityController cityController(CityService cityService) {
        return new CityController(cityService);
    }
}
