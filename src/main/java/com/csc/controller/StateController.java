package com.csc.controller;

import com.csc.model.State;
import com.csc.service.StateService;
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

@Tag(name = "States", description = "Retrieve states and provinces by country")
@RestController
@RequestMapping("${csc.base-path:/api/v1/geo}/states")
public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @Operation(
            summary = "List states by country",
            description = "Returns all states or provinces that belong to the given country")
    @Parameters({
        @Parameter(
                name = "countryCode",
                description = "ISO 3166-1 alpha-2 country code",
                example = "US",
                required = true),
        @Parameter(
                name = "fields",
                description =
                        "Comma-separated list of fields to include in the response (e.g. id,name,iso2). Defaults to id,name when omitted.",
                example = "id,name,iso2,countryCode")
    })
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "States retrieved successfully",
                content =
                        @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = State.class))))
    })
    @GetMapping(params = {"countryCode"})
    public List<State> getStatesByCountryCode(
            @RequestParam String countryCode, @RequestParam(required = false) List<String> fields) {
        return stateService.findStatesByCountryCode(countryCode, fields);
    }

    @Operation(
            summary = "Get a state by country and state code",
            description = "Returns the state matching the given country code and ISO 3166-2 state code")
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
                        "Comma-separated list of fields to include in the response (e.g. id,name,iso2). Defaults to id,name when omitted.",
                example = "id,name,iso2,countryCode")
    })
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "State found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = State.class))),
        @ApiResponse(responseCode = "404", description = "No state found for the given codes", content = @Content)
    })
    @GetMapping(params = {"countryCode", "stateCode", "fields"})
    public ResponseEntity<State> getStateByCountryCodeAndStateCode(
            @RequestParam String countryCode,
            @RequestParam(required = false) String stateCode,
            @RequestParam(required = false) List<String> fields) {
        return stateService
                .findStateByCountryCodeAndStateCode(countryCode, stateCode, fields)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
