package com.computingfacts.api.hateoas;

import com.computingfacts.api.controller.EnzymeController;
import com.computingfacts.api.model.EnzymeModel;
import com.computingfacts.api.util.ProteinUtil;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;

/**
 *
 * @author joseph
 */
@Component
public class PaginationModelAssembler implements RepresentationModelAssembler<EnzymeEntry, EnzymeModel> {

    private static final int LIMIT = 10;
    private static final String ENZYME = "enzyme";
    private static final String ASSOCIATED_PROTEINS = "associated Proteins";

    @Override
    public EnzymeModel toModel(EnzymeEntry enzyme) {

        Class<EnzymeController> controllerClass = EnzymeController.class;
        EnzymeModel model = buildEnzymeModel(enzyme);
        model.add(linkTo(methodOn(controllerClass).findEnzymeByEcNumber(enzyme.getEc())).withRel(ENZYME));
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
                .associatedProteins(ProteinUtil.toCollectionModel(enzyme.getProteinGroupEntry()))
                .build();
    }

}
