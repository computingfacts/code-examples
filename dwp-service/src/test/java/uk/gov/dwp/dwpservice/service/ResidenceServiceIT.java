package uk.gov.dwp.dwpservice.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import uk.gov.dwp.dwpservice.model.User;

/**
 *
 * @author joseph
 */
@Slf4j
@SpringBootTest
public class ResidenceServiceIT {

    private static final String CITY = "London";
    private static final double LAT = 51.509865;
    private static final double LON = -0.118092;
    private static final int RADIUS = 50;

    private static final User EXPECTED_USER = User.builder()
            .id(135)
            .firstName("Mechelle")
            .lastName("Boam")
            .email("mboam3q@thetimes.co.uk")
            .ipAddress("113.71.242.187")
            .latitude(-6.5115909)
            .longitude(105.652983)
            .city("London")
            .build();

    @Autowired
    private ResidenceService residenceService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(residenceService).isNotNull();

    }

    /**
     * Test of findUserById method, of class ResidenceServiceImpl.
     */
    @Test
    public void testUserById() {
        log.info("testUserById");
        String firstName = "Mechelle";
        String lastName = "Boam";
        String city = "London";

        int userId = 135;
        Mono<User> user = residenceService.findUserById(userId);

        StepVerifier
                .create(user)
                .expectNext(EXPECTED_USER)
                .verifyComplete();
        StepVerifier
                .create(user)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals(userId, res.getId());
                    assertEquals(firstName, res.getFirstName());
                    assertEquals(lastName, res.getLastName());
                    assertEquals(city, res.getCity());
                })
                .verifyComplete();
    }

    /**
     * Test of findUsersByCity method, of class ResidenceServiceImpl.
     */
    @Test
    public void testFindUsersByCity() {
        log.info("testFindUsersByCity");
        String city = "london";
        Flux<User> users = residenceService.findUsersByCity(city);

        StepVerifier
                .create(users)
                .assertNext(user -> {
                    assertNotNull(user);
                    assertThat(user.getCity()).isEqualToIgnoringCase(city);
                })
                .expectNextCount(5)
                .expectComplete()
                .verifyThenAssertThat()
                .tookLessThan(Duration.ofSeconds(30));

    }

    /**
     * Test of peopleInLondonAndSurroundings method, of class
     * ResidenceServiceImpl.
     */
    public void testPeopleInLondonAndSurroundings() {
        log.info("testPeopleInLondonAndSurroundings");

        List<User> expectedUsers
                = residenceService.findUsers()
                        .publishOn(Schedulers.boundedElastic())
                        .collectList()
                        .block();

        Flux<User> londoners = residenceService.findPeopleInCityAndSurroundings(CITY, LAT, LON, RADIUS);

        StepVerifier
                .create(londoners)
                .expectNextMatches(expectedUsers::contains)
                .expectNextMatches(expectedUsers::contains)
                .expectNextCount(4)
                .verifyComplete();

        StepVerifier
                .create(londoners)
                .assertNext(result -> assertThat(result)
                .hasFieldOrPropertyWithValue("city", CITY))
                .expectNextMatches(user -> user.getCity().equalsIgnoreCase(CITY))
                .expectNextMatches(user -> user.getCity().equalsIgnoreCase(CITY))
                .expectNextMatches(user -> user.getCity().equalsIgnoreCase(CITY))
                .expectNextMatches(user -> user.getCity().equalsIgnoreCase(CITY))
                .expectNextCount(1)
                .expectComplete()
                .verify();

    }

    /**
     * Test of peopleInCityAndSurroundings method, of class
     * ResidenceServiceImpl.
     */
    @Test
    public void testPeopleInCityAndSurroundings() {
        log.info("testPeopleInCityAndSurroundings");
        String city = "London";
        double lat = 51.509865;
        double lon = -0.118092;
        int radius = 10;
        Flux<User> people = residenceService.findPeopleInCityAndSurroundings(city, lat, lon, radius);

        StepVerifier
                .create(people)
                .assertNext(result -> {
                    assertNotNull(result);
                    assertThat(result).hasFieldOrPropertyWithValue("city", city);
                })
                .expectNextMatches(result -> result.getCity().equalsIgnoreCase(city))
                .expectNextCount(4)
                .expectComplete()
                .verify();

        StepVerifier
                .create(people.collectSortedList(Comparator.comparing(User::getCity).reversed()))
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res).isNotEmpty();
                    assertThat(res).hasSize(6);
                })
                .verifyComplete();

        StepVerifier
                .create(people.collectSortedList(Comparator.comparing(User::getId)))
                .expectSubscription()
                .recordWith(ArrayList::new)
                .expectNextCount(1)
                .consumeRecordedWith(u -> assertThat(u).asList().isNotEmpty())
                .consumeRecordedWith(u -> assertThat(contains(EXPECTED_USER)))
                .expectComplete()
                .verify();

    }

}
