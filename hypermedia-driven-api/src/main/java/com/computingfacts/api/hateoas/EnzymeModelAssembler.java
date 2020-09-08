package com.computingfacts.api.hateoas;

import com.computingfacts.api.controller.EnzymeController;
import com.computingfacts.api.model.EnzymeModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;

/**
 *
 * @author joseph
 */
@Component
public class EnzymeModelAssembler implements RepresentationModelAssembler<EnzymeEntry, EnzymeModel> {

    private static final int START = 0;
    private static final int LIMIT = 10;
    private static final String ENZYMES = "enzymes";
    private static final String ASSOCIATED_PROTEINS = "associated Proteins";

    private final ProteinModelAssembler proteinModelAssembler;

    public EnzymeModelAssembler(ProteinModelAssembler proteinModelAssembler) {
        this.proteinModelAssembler = proteinModelAssembler;
    }


    @Override
    public EnzymeModel toModel(EnzymeEntry enzyme) {
        EnzymeModel model = buildEnzymeModel(enzyme);
        Class<EnzymeController> controllerClass = EnzymeController.class;
        model.add(linkTo(methodOn(controllerClass).findEnzymeByEcNumber(enzyme.getEc())).withSelfRel());
        model.add(linkTo(methodOn(controllerClass).enzymes(START, LIMIT)).withRel(ENZYMES));
        model.add(linkTo(methodOn(controllerClass).findAssociatedProteinsByEcNumber(enzyme.getEc(), LIMIT)).withRel(ASSOCIATED_PROTEINS));

        return model;
    }

    private EnzymeModel buildEnzymeModel(EnzymeEntry enzyme) {
        return EnzymeModel.builder()
                .ecNumber(enzyme.getEc())
                .enzymeName(enzyme.getEnzymeName())
                .enzymeFamily(enzyme.getEnzymeFamily())
                .alternativeNames(enzyme.getAltNames())
                .catalyticActivities(enzyme.getCatalyticActivities())
                .cofactors(enzyme.getIntenzCofactors())
                .associatedProteins(proteinModelAssembler.toCollectionModel(enzyme.getProteinGroupEntry()))
                .build();
    }

}
