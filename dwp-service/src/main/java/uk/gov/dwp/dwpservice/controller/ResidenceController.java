package uk.gov.dwp.dwpservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import uk.gov.dwp.dwpservice.config.ApiProperties;
import uk.gov.dwp.dwpservice.model.User;
import uk.gov.dwp.dwpservice.service.ResidenceService;

/**
 *
 * @author joseph
 */

@RequestMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class ResidenceController {

    private final ResidenceService residenceService;
    private final ApiProperties apiProperties;

    @Autowired
    public ResidenceController(ResidenceService residenceService, ApiProperties apiProperties) {
        this.residenceService = residenceService;
        this.apiProperties = apiProperties;
    }

    @Operation(operationId = "londonArea", summary = "People in a given city and surrounding area", description = "Returns people who are listed as either living in the given city, or whose current coordinates are within given miles of the given city", tags = {"People"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class)))})
    @GetMapping(value = "/search")
    public Flux<User> londonArea(@Parameter(description = "city") @RequestParam(value = "city", defaultValue = "London", name = "city") String city,
            @Parameter(description = "Latitude") @RequestParam(value = "lat", defaultValue = "51.509865") double lat,
            @Parameter(description = "Longitude") @RequestParam(value = "lon", defaultValue = "-0.118092") double lon,
            @Parameter(description = "Radius in Miles") @RequestParam(value = "radius", defaultValue = "50") int radius) {

        return residenceService.findPeopleInCityAndSurroundings(city, lat, lon, radius);

    }

    @Operation(operationId = "londoners", summary = "People in London (lat=51.509865,lon=-0.118092) and within 50 miles", description = "Returns people who are listed as either living in London, or whose current coordinates are within 50 miles of London", tags = {"People"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class)))})
    @GetMapping(value = "/")
    public Flux<User> londoners() {
        return residenceService.findPeopleInCityAndSurroundings(apiProperties.getCity(), apiProperties.getLat(), apiProperties.getLon(), apiProperties.getRadius());

    }
}
