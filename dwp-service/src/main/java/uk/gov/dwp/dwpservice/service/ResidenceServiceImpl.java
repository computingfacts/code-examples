package uk.gov.dwp.dwpservice.service;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.gov.dwp.dwpservice.config.ApiProperties;
import uk.gov.dwp.dwpservice.model.User;
import uk.gov.dwp.dwpservice.util.GeoUtil;

/**
 *
 * @author joseph
 */
@Service("residenceService")
class ResidenceServiceImpl implements ResidenceService {

    private static final String USER_ENDPOINT = "/user/";
    private static final String USERS_ENDPOINT = "/users";
    private static final String CITY_ENDPOINT = "/city/";

    private final WebClient webClient;
    private final ApiProperties apiProperties;

    @Autowired
    public ResidenceServiceImpl(WebClient webClient, ApiProperties apiProperties) {
        this.webClient = webClient;
        this.apiProperties = apiProperties;
    }

    @Override
    public Flux<User> findUsers() {
        String url = String.format("%s%s", apiProperties.getBaseUrl(), USERS_ENDPOINT);
        return getReactiveApi(url, User.class)
                .flatMap(user -> findUserById(user.getId()));
    }

    @Override
    public Mono<User> findUserById(int userId) {
        String url = String.format("%s%s%s", apiProperties.getBaseUrl(), USER_ENDPOINT, userId);
        return webClient.get()
                .uri(buildURI(url))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(User.class))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<User> findUsersByCity(String city) {
        return usersByCity(city)
                .flatMap(user -> findUserById(user.getId()));
    }

    @Override
    public Flux<User> findPeopleInCityAndSurroundings(String city, double lat, double lon, int radius) {
        return usersWithinCoordinates(lat, lon, radius)
                .mergeWith(usersByCity(city))
                .flatMap(user -> findUserById(user.getId()));

    }

    private Flux<User> usersByCity(String city) {
        String cityName = WordUtils.capitalizeFully(city);
        String url = String.format("%s%s%s/users", apiProperties.getBaseUrl(), CITY_ENDPOINT, cityName);

        return getReactiveApi(url, User.class);

    }

    private Flux<User> usersWithinCoordinates(double lat, double lon, int radius) {
        return users()
                .filter(user -> GeoUtil.isWithinCoordinates(lat, user.getLatitude(), lon, user.getLongitude(), radius));
    }

    private Flux<User> users() {
        String url = String.format("%s%s", apiProperties.getBaseUrl(), USERS_ENDPOINT);
        return getReactiveApi(url, User.class);

    }

    private <T> Flux<T> getReactiveApi(String url, Class<T> resultType) {
        return webClient.get()
                .uri(buildURI(url))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(resultType))
                .switchIfEmpty(Flux.empty());
    }

    private URI buildURI(String endpoint) {
        String url = String.format("%s", endpoint);
        return UriComponentsBuilder.fromUriString(url).build().toUri();
    }

}
