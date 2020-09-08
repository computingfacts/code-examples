package com.computingfacts.api.controller;

import com.computingfacts.api.exceptions.ResourceNotFoundException;
import com.computingfacts.api.hateoas.ProteinModelAssembler;
import com.computingfacts.api.model.Disease;
import com.computingfacts.api.model.ProteinModel;
import com.computingfacts.api.service.ProteinApiService;
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
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author joseph
 */
@BasePathAwareController
@Tag(name = "Protein-centric", description = "Protein centred endpoints")
@RequestMapping(value = "/protein", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
@RestController
public class ProteinController {

    private final ProteinApiService proteinApiService;
    private final ProteinModelAssembler proteinModelAssembler;

    @Autowired
    public ProteinController(ProteinApiService proteinApiService, ProteinModelAssembler proteinModelAssembler) {
        this.proteinApiService = proteinApiService;
        this.proteinModelAssembler = proteinModelAssembler;
    }

    @Operation(operationId = "findProteinByAccession", summary = "Get Protein by Accession", description = "Find the protein with the associated Uniprot accession.", tags = {"Protein-centric"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProteinModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid Uniprot accession", content = @Content)})
    @GetMapping(value = "/{accession}")
    public ResponseEntity<ProteinModel> findProteinByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {

        if (Objects.isNull(accession)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return Optional.ofNullable(proteinApiService.proteinByAccession(accession))
                .map(proteinModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Protein with UniProt Accession=%s not found", accession)));

    }

    @GetMapping(value = "/{accession}/diseases")
    public CollectionModel<Disease> findDiseasesByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "O15297") @PathVariable("accession") String accession) {

        return CollectionModel.of(proteinApiService.diseasesByAccession(accession));
    }

    @Hidden
    @GetMapping(value = "/{accession}/proteinStructure")
    public ResponseEntity<?> findProteinStructureByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") @Valid String accession) {

        return todo();
    }

    @Hidden
    @GetMapping(value = "/{accession}/reaction")
    public ResponseEntity<?> findReactionsByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession, @Parameter(description = "number of reaction mechanism result to return") @RequestParam(value = "limit", defaultValue = "1") int limit) {
        return todo();

    }

    @Hidden
    @GetMapping(value = "/{accession}/pathways")
    public ResponseEntity<?> findPathwaysByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {

        return todo();
    }

    @Hidden
    @GetMapping(value = "/{accession}/smallmolecules")
    public ResponseEntity<?> findSmallmoleculesByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {

        return todo();
    }

    @Hidden
    @GetMapping(value = "/{accession}/citation")
    public ResponseEntity<?> findCitationsByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession, @Parameter(description = "citation result limit") @RequestParam(value = "limit", defaultValue = "1") int limit) {

        return todo();
    }

    private ResponseEntity<?> todo() {
        //not implemented
        return Optional.empty()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
