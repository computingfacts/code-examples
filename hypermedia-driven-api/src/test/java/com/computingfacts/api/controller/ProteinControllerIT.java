package com.computingfacts.api.controller;

import com.computingfacts.api.model.Disease;
import java.net.URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;

/**
 *
 * @author joseph
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProteinControllerIT {

    @LocalServerPort
    private int port;

    /**
     * Test of findProteinByAccession method, of class ProteinController.
     */
    @Test
    public void testFindProteinByAccession() {

        String accession = "O15297";
        String baseUrl = "http://localhost:" + this.port + "/computingfacts/rest/protein/" + accession;
        Traverson traverson = new Traverson(URI.create(baseUrl), MediaTypes.HAL_JSON);

        CollectionModel<Disease> diseases = traverson
                .follow("protein", "diseases")
                .toObject(new TypeReferences.CollectionModelType<Disease>() {
                });

        assertNotNull(diseases);
        assertNotNull(diseases.getContent());

        assertThat(diseases.getContent()).hasSize(3);
        assertThat(diseases.getContent()).asString().contains("Breast cancer (BC)");


    }

}
