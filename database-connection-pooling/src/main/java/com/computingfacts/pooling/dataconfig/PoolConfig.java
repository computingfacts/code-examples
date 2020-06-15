package com.computingfacts.pooling.dataconfig;

import com.computingfacts.pooling.config.ConfigProperties;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author joseph
 */
@Configuration
public class PoolConfig {

    @Autowired
    private ConfigProperties configProperties;

    @Bean(name = "dataSourceCp30")
    public DataSource dataSourceCp30() {
        ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
        pooledDataSource.setJdbcUrl(configProperties.getUrl());
        pooledDataSource.setUser(configProperties.getUsername());
        pooledDataSource.setPassword(configProperties.getPassword());
        pooledDataSource.setMinPoolSize(2);
        return pooledDataSource;
    }

    @Bean(name = "dataSourceDBCP")
    public DataSource dataSourceDBCP() {

        BasicDataSource dbcpDataSource = new BasicDataSource();
        dbcpDataSource.setUrl(configProperties.getUrl());
        dbcpDataSource.setUsername(configProperties.getUsername());
        dbcpDataSource.setPassword(configProperties.getPassword());
        dbcpDataSource.setInitialSize(1);

        return dbcpDataSource;
    }

    @Bean(name = "dataSourceHikariCP")
    public DataSource dataSourceHikariCP() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(configProperties.getUrl());
        config.setUsername(configProperties.getUsername());
        config.setPassword(configProperties.getPassword());

        //more config
        config.setPoolName("example-pool");
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);
        config.addDataSourceProperty("elideSetAutoCommits", true);
        config.addDataSourceProperty("maintainTimeStats", false);

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

    @Bean(name = "dataSourceTomcatJdbc")
    public DataSource dataSourceTomcatJdbc() {

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
