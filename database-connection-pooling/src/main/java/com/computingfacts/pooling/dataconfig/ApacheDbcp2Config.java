package com.computingfacts.pooling.dataconfig;


import com.computingfacts.pooling.config.ConfigProperties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author joseph
 */
@Profile("dbcp2")
@Configuration
public class ApacheDbcp2Config {

    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dbcpDataSource = new BasicDataSource();
        //dbcpDataSource.setDriverClassName("org.postgresql.Driver");
        dbcpDataSource.setUrl(configProperties.getUrl());
        dbcpDataSource.setUsername(configProperties.getUsername());
        dbcpDataSource.setPassword(configProperties.getPassword());
        dbcpDataSource.setInitialSize(1);
        return dbcpDataSource;
    }
}
