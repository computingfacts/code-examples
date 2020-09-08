package com.computingfacts.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 *
 * @author joseph
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Relation(collectionRelation = "enzymes", itemRelation = "enzymeModel")
@JsonPropertyOrder({"enzymeName", "ecNumber", "enzymeFamily", "alternativeNames", "catalyticActivities", "cofactors", "associatedProteins"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class EnzymeModel extends RepresentationModel<EnzymeModel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "enzyme name", example = "Alcohol dehydrogenase")
    @JsonProperty("enzymeName")
    private String enzymeName;
    @Schema(description = "Enzyme Classfication (EC) number", example = "1.1.1.1", required = true)
    @NotBlank
    @EqualsAndHashCode.Include
    @JsonProperty("ecNumber")
    private String ecNumber; //uniquely identifies an Enzyme
    @JsonProperty("enzymeFamily")
    private String enzymeFamily;
    @JsonProperty("alternativeNames")
    private Set<String> alternativeNames;
    @JsonProperty("catalyticActivities")
    private List<String> catalyticActivities;
    @JsonProperty("cofactors")
    private Set<String> cofactors;

    @JsonProperty("associatedProteins")
    private CollectionModel<ProteinModel> associatedProteins;


}
