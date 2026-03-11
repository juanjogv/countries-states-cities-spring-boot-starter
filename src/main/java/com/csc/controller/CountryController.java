package com.csc.controller;

import com.csc.model.Country;
import com.csc.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Countries", description = "Retrieve countries with their geographical, currency and timezone metadata")
@RestController
@RequestMapping("${csc.base-path:/api/v1/geo}/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(summary = "List all countries", description = "Returns all 250 countries ordered by name")
    @Parameters({
        @Parameter(
                name = "fields",
                description =
                        "Comma-separated list of fields to include in the response (e.g. id,name,iso2). Defaults to id,name when omitted.",
                example = "id,name,iso2,capital")
    })
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Countries retrieved successfully",
                content =
                        @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = Country.class))))
    })
    @GetMapping
    public List<Country> getCountries(@RequestParam(required = false) List<String> fields) {
        return countryService.findAllCountries(fields);
    }

    @Operation(
            summary = "Get a country by ISO 2 code",
            description = "Returns the country matching the given ISO 3166-1 alpha-2 code")
    @Parameters({
        @Parameter(name = "countryCode", description = "ISO 3166-1 alpha-2 country code", example = "US"),
        @Parameter(
                name = "fields",
                description =
                        "Comma-separated list of fields to include in the response (e.g. id,name,iso2). Defaults to id,name when omitted.",
                example = "id,name,iso2,capital")
    })
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Country found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Country.class))),
        @ApiResponse(responseCode = "404", description = "No country found for the given code", content = @Content)
    })
    @GetMapping(params = {"countryCode"})
    public ResponseEntity<Country> getCountryByCode(
            @RequestParam(required = false) String countryCode, @RequestParam(required = false) List<String> fields) {
        return countryService
                .findCountryByCode(countryCode, fields)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
