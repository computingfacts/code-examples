package uk.gov.dwp.dwpservice.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import uk.gov.dwp.dwpservice.model.User;

/**
 *
 * @author joseph
 */
@Slf4j
@SpringBootTest
public class ResidenceServiceTest {
    private   static  final User EXPECTED_USER = User.builder()
            .id(135)
            .firstName("Mechelle")
            .lastName("Boam")
            .email("mboam3q@thetimes.co.uk")
            .ipAddress("113.71.242.187")
            .latitude(-6.5115909)
            .longitude(105.652983)
            .city("London")
            .build();
    @Mock
    private ResidenceService residenceService;

    /**
     * Test of findUserById method, of class ResidenceServiceImpl.
     */
    @Test
    public void testUserById() {
        log.info("testUserById");

        Mono<User> expectedUser = Mono.just(EXPECTED_USER);
        int userId = 135;
        when(residenceService.findUserById(userId)).thenReturn(expectedUser);
        Mono<User> userWithId = residenceService.findUserById(userId);

        assertThat(userWithId).isNotNull();
        assertTrue(userWithId.hasElement().block());
    }

    /**
     * Test of findUsersByCity method, of class ResidenceServiceImpl.
     */
    @Test
    public void testFindUsersByCity() {
        log.info("testFindUsersByCity");
        Flux<User> expectedUsers = Flux.just(EXPECTED_USER);
        String city = "london";
        when(residenceService.findUsersByCity(city)).thenReturn(expectedUsers);
        Flux<User> users = residenceService.findUsersByCity(city);
        assertNotNull(users);
       List<User> u =  users
               .publishOn(Schedulers.boundedElastic())
                .collectList()
                .block();
        assertTrue(!u.isEmpty());
        assertThat(u).hasSizeGreaterThanOrEqualTo(1);


    }

    /**
     * Test of peopleInCityAndSurroundings method, of class
     * ResidenceServiceImpl.
     */
    @Test
    public void testPeopleInCityAndSurroundings() {
        log.info("testPeopleInCityAndSurroundings");
        Flux<User> userList = Flux.just(EXPECTED_USER);
        String city = "London";
        double lat = 51.509865;
        double lon = -0.118092;
        int radius = 10;

        when(residenceService.findPeopleInCityAndSurroundings(city, lat, lon, radius)).thenReturn(userList);
        Flux<User> people = residenceService.findPeopleInCityAndSurroundings(city, lat, lon, radius);
        assertNotNull(people);
    }
    }
