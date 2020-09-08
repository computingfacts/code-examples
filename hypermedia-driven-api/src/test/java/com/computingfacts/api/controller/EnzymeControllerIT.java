package com.computingfacts.api.controller;

import com.computingfacts.api.model.EnzymeModel;
import com.computingfacts.api.model.ProteinModel;
import java.net.URI;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;

/**
 *
 * @author joseph
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EnzymeControllerIT {

    @LocalServerPort
    private int port;

    private Traverson getTraversonClient(String endpoint) {
        String url = "http://localhost:" + this.port + "/computingfacts/rest/" + endpoint;
        return new Traverson(URI.create(url), MediaTypes.HAL_JSON);
    }

    @Test
    public void testFindEnzymeByEcNumber() {
        String ec = "1.1.1.1";
        String endPoint = "enzymes/" + ec;
        Traverson traverson = getTraversonClient(endPoint);

        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        EnzymeModel enzymeModel = client.toObject(EnzymeModel.class);

        assertNotNull(enzymeModel);

        assertEquals("1.1.1.1", enzymeModel.getEcNumber());
        assertEquals("Alcohol dehydrogenase", enzymeModel.getEnzymeName());
        assertEquals("Oxidoreductases", enzymeModel.getEnzymeFamily());
        assertThat(enzymeModel.getCatalyticActivities()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(enzymeModel.getAssociatedProteins()).hasSizeGreaterThanOrEqualTo(2);
        assertThat(enzymeModel.getAlternativeNames()).containsAnyOf("Aldehyde reductase.");

        //links
        assertTrue(enzymeModel.hasLinks());
        assertTrue(enzymeModel.getLinks().hasSize(3));
        assertTrue(enzymeModel.hasLink(LinkRelation.of(IanaLinkRelations.SELF.value())));
        assertTrue(enzymeModel.hasLink(LinkRelation.of("enzymes")));
        assertTrue(enzymeModel.hasLink("associated Proteins"));

        String proteinsHref = "/enzymes/{ec}/proteins{?limit}";
        Link proteinLink = Link.of(proteinsHref);

        assertThat(proteinLink.isTemplated()).isTrue();
        assertThat(proteinLink.getVariableNames()).contains("ec", "limit");

        CollectionModel<ProteinModel> proteins = traverson.follow("associated Proteins")
                .toObject(new TypeReferences.CollectionModelType<ProteinModel>() {
                });

        assertNotNull(proteins);
        assertNotNull(proteins.getContent());
        assertTrue(proteins.hasLinks());
        assertTrue(proteins.hasLink(IanaLinkRelations.SELF));
        assertThat(proteins.getContent()).hasSize(10);

    }

    @Test
    public void testEnzymes() {
        String endPoint = "enzymes/";
        Traverson traverson = getTraversonClient(endPoint);
        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        PagedModel<EnzymeModel> enzymes = client
                .toObject(new TypeReferences.PagedModelType<EnzymeModel>() {
                });

        assertNotNull(enzymes);
        assertNotNull(enzymes.getContent());
        assertTrue(enzymes.hasLinks());

        assertTrue(enzymes.hasLink(IanaLinkRelations.FIRST));
        assertTrue(enzymes.hasLink(IanaLinkRelations.SELF));
        assertTrue(enzymes.hasLink(IanaLinkRelations.NEXT));
        assertTrue(enzymes.hasLink(IanaLinkRelations.LAST));

        //page object
        assertNotNull(enzymes.getMetadata());
        assertTrue(enzymes.getMetadata().getNumber() == 0);
        assertTrue(enzymes.getMetadata().getTotalPages() > 100);
        assertTrue(enzymes.getMetadata().getTotalElements() > 1_000);
        assertTrue(enzymes.getMetadata().getSize() == 10);

        List<String> enzymeNames = client.toObject("$._embedded.enzymes.[*].enzymeName");
        List<String> associatedProteinsAccession = client.toObject("$._embedded.enzymes.[0].associatedProteins._embedded.proteins[*].accession");

        assertThat(enzymeNames).hasSizeGreaterThanOrEqualTo(10);
        assertThat(associatedProteinsAccession).hasSizeGreaterThanOrEqualTo(2);

    }

}
