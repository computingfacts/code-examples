package com.computingfacts.api.hateoas;

import com.computingfacts.api.controller.ProteinController;
import com.computingfacts.api.model.ProteinModel;
import com.computingfacts.api.util.ProteinUtil;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;

/**
 *
 * @author joseph
 */
public class ProteinModelAssembler extends RepresentationModelAssemblerSupport<ProteinGroupEntry, ProteinModel> {

    private static final int LIMIT = 10;
    private static final String PROTEIN_STRUCTURE = "protein structure";
    private static final String REACTIONS = "reactions";
    private static final String PATHWAYS = "pathways";
    private static final String SMALL_MOLECULES = "small molecules";
    private static final String DISEASES = "diseases";
    private static final String LITERATURE = "literature";
    private static final String PROTEIN = "protein";

    public ProteinModelAssembler(Class<?> controllerClass, Class<ProteinModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public ProteinModel toModel(ProteinGroupEntry protein) {
        ProteinModel model = ProteinUtil.toProteinModel(protein);
        Class<ProteinController> controllerClass = ProteinController.class;
        model.add(linkTo(methodOn(controllerClass).findProteinByAccession(protein.getPrimaryAccession())).withRel(PROTEIN));
        model.add(linkTo(methodOn(controllerClass).findProteinStructureByAccession(protein.getPrimaryAccession())).withRel(PROTEIN_STRUCTURE));
        model.add(linkTo(methodOn(controllerClass).findReactionsByAccession(protein.getPrimaryAccession(), LIMIT)).withRel(REACTIONS));
        model.add(linkTo(methodOn(controllerClass).findPathwaysByAccession(protein.getPrimaryAccession())).withRel(PATHWAYS));
        model.add(linkTo(methodOn(controllerClass).findSmallmoleculesByAccession(protein.getPrimaryAccession())).withRel(SMALL_MOLECULES));
        model.add(linkTo(methodOn(controllerClass).findDiseasesByAccession(protein.getPrimaryAccession())).withRel(DISEASES));
        model.add(linkTo(methodOn(controllerClass).findCitationsByAccession(protein.getPrimaryAccession(), LIMIT)).withRel(LITERATURE));
        return model;

    }

    @Override
    public CollectionModel<ProteinModel> toCollectionModel(Iterable<? extends ProteinGroupEntry> entries) {
        return StreamSupport
                .stream(entries.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));

    }

}
