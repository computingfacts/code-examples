package uk.gov.dwp.dwpservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.endpoint")
public class ApiProperties {

    private String baseUrl;
    private String city;
    private double lat;
    private double lon;
    private int radius;

}
