package uk.gov.dwp.dwpservice.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.openapi.springdoc")
public class SpringDocProperties {

    private String contactName;
    private String contactEmail;
    private String licenseName;
    private String licenseUrl;
    private String serverUrl;
    private String serverDesc;
    private String apiTitle;
    private String apiDesc;
    private String apiVersion;
    private String apiTos;

}
