package com.computingfacts.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@JsonPropertyOrder({"accession", "proteinName", "organismName"})
@Relation(collectionRelation = "proteins", itemRelation = "proteinModel")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProteinModel extends RepresentationModel<ProteinModel> implements Serializable {

    private static final long serialVersionUID = 1L;
    @EqualsAndHashCode.Include
    @Schema(name = "accession", description = "Uniprot accession", example = "P07327", required = true)
    @JsonProperty("accession")
    private String accession; //uniquely identifies a Protein

    @Schema(description = "recommended protein name", example = "Alcohol dehydrogenase")
    @JsonProperty("proteinName")
    private String proteinName;

    @Schema(description = "organism's name", example = "Human")
    @JsonProperty("organismName")
    private String organismName;

}
