package uk.gov.dwp.dwpservice.controller;

import java.net.URI;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import uk.gov.dwp.dwpservice.config.ApiProperties;
import uk.gov.dwp.dwpservice.model.User;
import uk.gov.dwp.dwpservice.service.ResidenceService;

/**
 *
 * @author joseph
 */
@Slf4j
@Import(ApiProperties.class)
@WebFluxTest(controllers = ResidenceController.class)
public class ResidenceControllerTest {

    private static final String CITY = "London";
    private static final double LAT = 51.509865;
    private static final double LON = -0.118092;
    private static final int RADIUS = 50;
    @MockBean
    private ResidenceService residenceService;


    @Autowired
    private WebTestClient webTestClient;

    private static Flux<User> users;

    @BeforeAll
    public static void testDataInit() {

        User user1 = User.builder()
                .id(135)
                .firstName("Mechelle")
                .lastName("Boam")
                .email("mboam3q@thetimes.co.uk")
                .ipAddress("113.71.242.187")
                .latitude(-6.5115909)
                .longitude(105.652983)
                .city("London")
                .build();
        User user2 = User.builder()
                .id(136)
                .firstName("Jeff")
                .lastName("Boam")
                .email("jeff@thetimes.co.uk")
                .ipAddress("113.71.242.187")
                .latitude(-6.5115909)
                .longitude(105.652983)
                .city("London")
                .build();
        users = Flux.fromIterable(Arrays.asList(user1, user2));
    }



    /**
     * Test of londoners method, of class ResidenceController.
     */
    @Test
    void testLondoners() throws Exception {


        Mockito.when(residenceService.findPeopleInCityAndSurroundings(CITY, LAT, LON, RADIUS)).thenReturn(users);

             webTestClient.get()
                .uri(URI.create("/"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
             .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
             .expectBodyList(User.class)
             .value(users ->
                users.forEach( u ->
                        assertTrue(u.getCity().contains("London"))));


    }
    /**
     * Test of londonArea method, of class ResidenceController.
     */
    @Test
    void testLondonArea() throws Exception {

        Mockito.when(residenceService.findPeopleInCityAndSurroundings(CITY, LAT, LON, RADIUS)).thenReturn(users);

        webTestClient.get()
                .uri(URI.create("/search"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(User.class)
                .value(users ->
                        users.forEach( u -> assertTrue(u.getCity().contains("London"))));
    }

}
