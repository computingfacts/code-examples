package com.computingfacts.pooling.dataconfig;

import com.computingfacts.pooling.config.ConfigProperties;
import javax.sql.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author joseph
 */
@Profile("tomcat")
@Configuration
public class TomcatConfig {

    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public DataSource dataSource() {

        PoolProperties p = new PoolProperties();
        p.setUrl(configProperties.getUrl());
        p.setDriverClassName("com.mysql.cj.jdbc.Driver");
        p.setUsername(configProperties.getUsername());
        p.setPassword(configProperties.getPassword());
        //more setting
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource(p);
        return ds;

    }
}
