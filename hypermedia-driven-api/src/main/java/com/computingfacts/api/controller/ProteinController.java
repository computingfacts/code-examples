package com.computingfacts.api.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.MediaTypes;
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
@Hidden
@BasePathAwareController
@Tag(name = "Protein-centric", description = "Protein centred endpoints")
@RequestMapping(value = "/protein", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
@RestController
public class ProteinController {

    @GetMapping(value = "/{accession}")
    public ResponseEntity<?> findProteinByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {

        return todo();
    }

    @GetMapping(value = "/{accession}/proteinStructure")
    public ResponseEntity<?> findProteinStructureByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") @Valid String accession) {

        return todo();
    }

    @GetMapping(value = "/{accession}/reaction")
    public ResponseEntity<?> findReactionsByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession, @Parameter(description = "number of reaction mechanism result to return") @RequestParam(value = "limit", defaultValue = "1") int limit) {
        return todo();

    }

    @GetMapping(value = "/{accession}/pathways")
    public ResponseEntity<?> findPathwaysByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {

        return todo();
    }

    @GetMapping(value = "/{accession}/smallmolecules")
    public ResponseEntity<?> findSmallmoleculesByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {

        return todo();
    }

    @GetMapping(value = "/{accession}/diseases")
    public ResponseEntity<?> findDiseasesByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "O15297") @PathVariable("accession") String accession) {
        return todo();
    }

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
