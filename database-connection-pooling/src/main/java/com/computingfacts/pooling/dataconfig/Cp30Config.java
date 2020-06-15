package com.computingfacts.pooling.dataconfig;

import com.computingfacts.pooling.config.ConfigProperties;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author joseph
 */
@Profile("cp30")
@Configuration
public class Cp30Config {

    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
        pooledDataSource.setJdbcUrl(configProperties.getUrl());
        pooledDataSource.setUser(configProperties.getUsername());
        pooledDataSource.setPassword(configProperties.getPassword());
       
        return pooledDataSource;
    }
}
