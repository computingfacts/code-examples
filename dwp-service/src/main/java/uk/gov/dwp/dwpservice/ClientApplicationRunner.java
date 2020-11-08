package uk.gov.dwp.dwpservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import uk.gov.dwp.dwpservice.model.User;
import uk.gov.dwp.dwpservice.service.ResidenceService;

/**
 *
 * @author joseph
 */
@Slf4j
@Component
public class ClientApplicationRunner implements CommandLineRunner {

    private static final String CITY = "london";
    private static final double LAT = 51.509865;
    private static final double LON = -0.118092;
    private static final int RADIUS = 10;
    @Autowired
    private ResidenceService residenceService;

    @Override
    public void run(String... args) throws Exception {

        Flux<User> users = residenceService.findPeopleInCityAndSurroundings(CITY, LAT, LON, RADIUS);
        users.doOnNext(people -> log.info("People in London Area :   " + people))
                .blockLast();

    }

}
