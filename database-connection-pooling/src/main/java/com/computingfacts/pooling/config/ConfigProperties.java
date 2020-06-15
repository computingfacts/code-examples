package com.computingfacts.pooling.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:application.properties"})
public class ConfigProperties {

    private String url;
    private String username;
    private String password;
    @Autowired
    private Environment env;

    public String getUrl() {
        url = env.getProperty("spring.datasource.url");
        return url;
    }

    public String getUsername() {
        username = env.getProperty("spring.datasource.username");
        return username;
    }

    public String getPassword() {
        password = env.getProperty("spring.datasource.password");
        return password;
    }

}
