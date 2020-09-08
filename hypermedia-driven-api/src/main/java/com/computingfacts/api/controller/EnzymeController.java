package com.computingfacts.api.controller;

import com.computingfacts.api.exceptions.InvalidInputException;
import com.computingfacts.api.exceptions.ResourceNotFoundException;
import com.computingfacts.api.hateoas.EnzymeModelAssembler;
import com.computingfacts.api.hateoas.PaginationModelAssembler;
import com.computingfacts.api.hateoas.ProteinModelAssembler;
import com.computingfacts.api.model.EnzymeModel;
import com.computingfacts.api.model.ProteinModel;
import com.computingfacts.api.service.EnzymeService;
import com.computingfacts.api.util.EcValidator;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import java.util.Optional;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;

/**
 *
 * @author joseph
 */
@Slf4j
@Tag(name = "Enzyme Entry", description = "Search enzyme by valid complete EC number")
@RequestMapping(value = "/enzymes", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
@RestController
public class EnzymeController {

    private final EnzymeModelAssembler enzymeModelAssembler;
    private final EnzymeService enzymeService;
    private final PagedResourcesAssembler<EnzymeEntry> pagedResourcesAssembler;
    private final PaginationModelAssembler paginationModelAssembler;
    private final ProteinModelAssembler proteinModelAssembler;

    @Autowired
    public EnzymeController(ProteinModelAssembler proteinModelAssembler, PaginationModelAssembler paginationModelAssembler, EnzymeModelAssembler enzymeModelAssembler, EnzymeService enzymeService, PagedResourcesAssembler<EnzymeEntry> pagedResourcesAssembler) {

        this.enzymeModelAssembler = enzymeModelAssembler;
        this.enzymeService = enzymeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.paginationModelAssembler = paginationModelAssembler;
        this.proteinModelAssembler = proteinModelAssembler;
    }

    @GetMapping(value = "/")
    public PagedModel<EnzymeModel> enzymes(@Parameter(description = "page number") @RequestParam(value = "page", defaultValue = "0", name = "page") int page, @Parameter(description = " result limit") @RequestParam(value = "size", defaultValue = "10",name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "ecNumber");
        Page<EnzymeEntry> enzymes = enzymeService.getEnzymeEntries(pageable);
        return pagedResourcesAssembler.toModel(enzymes, paginationModelAssembler);
    }

    @Operation(operationId = "findEnzymeByEcNumber", summary = "Get Enzyme by EC number", description = "Find the enzyme with the given valid EC number", tags = {"Enzyme-centric"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid EC number", content = @Content)})
    @GetMapping(value = "/{ec}")
    public ResponseEntity<EnzymeModel> findEnzymeByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") @Size(min = 7, max = 7) String ec) {
        validateEC(ec);
        return enzymeService.getEnzyme(ec)
                .map(enzyme -> enzymeModelAssembler.toModel(enzyme))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Enzyme with ec=%s not found", ec)));

    }


    @GetMapping(value = "/{ec}/proteins")
    public ResponseEntity<CollectionModel<ProteinModel>> findAssociatedProteinsByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") String ec, @Parameter(description = " result limit") @RequestParam(value = "limit", defaultValue = "10") int limit) {
        Link selfLink = linkTo(methodOn(EnzymeController.class).findAssociatedProteinsByEcNumber(ec, limit)).withSelfRel();
        return ResponseEntity.ok(proteinModelAssembler.toCollectionModel(enzymeService.getProteinsByEc(ec, limit)).add(selfLink));
    }

    @Hidden
    @GetMapping(value = "/{ec}/kineticParameters")
    public ResponseEntity<?> findKineticParametersByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") String ec, @Parameter(description = " result limit") @RequestParam(value = "limit", defaultValue = "10") int limit) {
        //not implemented
        return todo();
    }

    @Hidden
    @GetMapping(value = "/{ec}/mechanisms")
    public ResponseEntity<?> findMechanismsByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") String ec) {

        //not implemented
        return todo();
    }

    @Hidden
    @GetMapping(value = "/{ec}/citations")
    public ResponseEntity<?> findCitationByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") String ec, @Parameter(description = "citation result limit") @RequestParam(value = "limit", defaultValue = "10") @PositiveOrZero int limit) {
        //not implemented
        return todo();
    }

    private ResponseEntity<?> todo() {

        return Optional.empty()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private void validateEC(String ec) {
        if (Objects.isNull(ec)) {
            throw new InvalidInputException(String.format("Invalid EC number %s", ec));
        }
        boolean isEc = EcValidator.validateEc(ec);
        if (!isEc) {
            throw new InvalidInputException(String.format("Invalid EC number %s", ec));

        }

    }

}
