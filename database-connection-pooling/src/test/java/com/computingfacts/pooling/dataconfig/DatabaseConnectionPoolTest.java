package com.computingfacts.pooling.dataconfig;

import com.computingfacts.pooling.config.ConfigProperties;
import com.computingfacts.pooling.service.ApplicationOperation;
import com.computingfacts.pooling.service.ApplicationOperationImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 *
 * @author joseph
 */
@ActiveProfiles("dbcp2")
@Slf4j
@SpringJUnitConfig({ApacheDbcp2Config.class, Cp30Config.class, HikaricpConfig.class, TomcatConfig.class, ApplicationOperationImpl.class, ConfigProperties.class})
public class DatabaseConnectionPoolTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Environment env;
    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private ApplicationOperation operation;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(configProperties).isNotNull();
        assertThat(env).isNotNull();

        String activeProfile = Arrays.asList(env.getActiveProfiles())
                .stream()
                .findFirst()
                .orElse("hikaricp");

        if (activeProfile.equals("hikaricp")) {
            assertThat(dataSource).isInstanceOf(HikariDataSource.class);

        }
        if (activeProfile.equals("cp30")) {

            assertThat(dataSource).isInstanceOf(ComboPooledDataSource.class);

        }
        if (activeProfile.equals("tomcat")) {

            assertThat(dataSource).isInstanceOf(org.apache.tomcat.jdbc.pool.DataSource.class);

        }
        if (activeProfile.equals("dbcp2")) {

            assertThat(dataSource).isInstanceOf(BasicDataSource.class);

        }

    }

    /**
     * Test of doSomething method, of class ApplicationOperation.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDoSomething() throws SQLException {

        assertThat(operation).isNotNull();

        assertTrue(dataSource.getConnection().isValid(1));

        operation.doSomething(true);

    }
}
