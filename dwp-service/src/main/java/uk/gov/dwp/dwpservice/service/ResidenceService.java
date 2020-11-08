package uk.gov.dwp.dwpservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.gov.dwp.dwpservice.model.User;

/**
 *
 * @author joseph
 */
public interface ResidenceService {

    Mono<User> findUserById(int userId);

    Flux<User> findUsersByCity(String city);

    Flux<User> findUsers();

    Flux<User> findPeopleInCityAndSurroundings(String city, double lat, double lon, int radius);

}
