package com.computingfacts.api.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author joseph
 */
@Hidden
@RestController
public class IndexController {

    @RequestMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
    public ResponseEntity<RepresentationModel> index() {

        int page = 0;
        int pageSize = 10;
        RepresentationModel model = new RepresentationModel();

        model.add(linkTo(methodOn(IndexController.class).index()).withSelfRel());
        model.add(linkTo(methodOn(EnzymeController.class).enzymes(page, pageSize)).withRel("enzymes"));
        model.add(linkTo(methodOn(EnzymeController.class).findEnzymeByEcNumber("1.1.1.1")).withRel("enzyme"));
        model.add(linkTo(methodOn(EnzymeController.class).findAssociatedProteinsByEcNumber("1.1.1.1", pageSize)).withRel("associated proteins"));
        model.add(linkTo(methodOn(ProteinController.class).findProteinByAccession("P1234")).withRel("protein"));

        return ResponseEntity.ok(model);
    }
}
