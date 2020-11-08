package uk.gov.dwp.dwpservice.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Configuration
public class OpenApiSwaggerConfig {

    private final SpringDocProperties properties;

    @Autowired
    public OpenApiSwaggerConfig(SpringDocProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI openAPI() {

        Contact contact = new Contact();
        contact.setName(properties.getContactName());
        contact.setEmail(properties.getContactEmail());

        License license = new License();
        license.setName(properties.getLicenseName());
        license.setUrl(properties.getLicenseUrl());

        Server server = new Server();
        server.setDescription(properties.getServerDesc());
        server.setUrl(properties.getServerUrl());

        return new OpenAPI()
                .info(new Info()
                        .title(properties.getApiTitle())
                        .description(properties.getApiDesc())
                        .version(properties.getApiVersion())
                        .termsOfService(properties.getApiTos())
                        .license(license)
                        .contact(contact))
                .servers(Collections.singletonList(server));

    }

}
