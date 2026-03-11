package com.csc.controller;

import com.csc.model.City;
import com.csc.service.CityService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cities", description = "Retrieve cities filtered by country and optionally by state")
@RestController
@RequestMapping("${csc.base-path:/api/v1/geo}/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Operation(summary = "List cities by country", description = "Returns all cities that belong to the given country")
    @Parameters({
        @Parameter(
                name = "countryCode",
                description = "ISO 3166-1 alpha-2 country code",
                example = "US",
                required = true),
        @Parameter(
                name = "fields",
                description =
                        "Comma-separated list of fields to include in the response (e.g. id,name,stateCode). Defaults to id,name when omitted.",
                example = "id,name,stateCode,countryCode")
    })
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cities retrieved successfully",
                content =
                        @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = City.class))))
    })
    @GetMapping(params = {"countryCode"})
    public List<City> getCitiesByCountryCode(
            @RequestParam String countryCode, @RequestParam(required = false) List<String> fields) {
        return cityService.findCitiesByCountryCode(countryCode, fields);
    }

    @Operation(
            summary = "List cities by country and state",
            description = "Returns all cities that belong to the given country and state")
    @Parameters({
        @Parameter(
                name = "countryCode",
                description = "ISO 3166-1 alpha-2 country code",
                example = "US",
                required = true),
        @Parameter(name = "stateCode", description = "ISO 3166-2 state code", example = "CA"),
        @Parameter(
                name = "fields",
                description =
                        "Comma-separated list of fields to include in the response (e.g. id,name,stateCode). Defaults to id,name when omitted.",
                example = "id,name,stateCode,countryCode")
    })
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cities retrieved successfully",
                content =
                        @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = City.class))))
    })
    @GetMapping(params = {"countryCode", "stateCode"})
    public List<City> getCitiesByCountryCodeAndStateCode(
            @RequestParam String countryCode,
            @RequestParam(required = false) String stateCode,
            @RequestParam(required = false) List<String> fields) {
        return cityService.findCitiesByCountryCodeAndStateCode(countryCode, stateCode, fields);
    }
}
