package uk.gov.dwp.dwpservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import uk.gov.dwp.dwpservice.config.ApiProperties;
import uk.gov.dwp.dwpservice.service.ResidenceService;

@SpringBootTest
class DwpServiceApplicationTests {

    @Autowired
    private ResidenceService residenceService;
    @Autowired
    private WebClient webClient;
    @Autowired
    private ApiProperties apiProperties;

    @Test
    void contextLoads() {
        assertThat(residenceService).isNotNull();
        assertThat(webClient).isNotNull();
        assertThat(apiProperties).isNotNull();

    }

}
