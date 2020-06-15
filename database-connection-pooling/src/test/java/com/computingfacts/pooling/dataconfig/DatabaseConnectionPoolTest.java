package com.computingfacts.pooling.dataconfig;

import com.computingfacts.pooling.config.ConfigProperties;
import com.computingfacts.pooling.service.ApplicationOperation;
import com.computingfacts.pooling.service.ApplicationOperationImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 *
 * @author joseph
 */
@Slf4j
@SpringJUnitConfig({ApplicationOperationImpl.class, ConfigProperties.class, PoolConfig.class})
public class DatabaseConnectionPoolTest {

    @Autowired
    private DataSource dataSourceHikariCP;
    @Autowired
    private DataSource dataSourceDBCP;
    @Autowired
    private DataSource dataSourceCp30;
    @Autowired
    private DataSource dataSourceTomcatJdbc;

    @Autowired
    private ApplicationOperation operation;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSourceHikariCP).isNotNull();
        assertThat(dataSourceDBCP).isNotNull();
        assertThat(dataSourceCp30).isNotNull();
        assertThat(dataSourceTomcatJdbc).isNotNull();

        assertThat(dataSourceHikariCP).isInstanceOf(HikariDataSource.class);
        assertThat(dataSourceDBCP).isInstanceOf(BasicDataSource.class);
        assertThat(dataSourceCp30).isInstanceOf(ComboPooledDataSource.class);
        assertThat(dataSourceTomcatJdbc).isInstanceOf(org.apache.tomcat.jdbc.pool.DataSource.class);

    }

    /**
     * Test of doSomething method, of class ApplicationOperation.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testDoSomething() throws SQLException {

        assertThat(operation).isNotNull();

        assertTrue(dataSourceHikariCP.getConnection().isValid(1));
        assertTrue(dataSourceDBCP.getConnection().isValid(1));
        assertTrue(dataSourceCp30.getConnection().isValid(1));
        assertTrue(dataSourceTomcatJdbc.getConnection().isValid(1));

        operation.doSomething(dataSourceHikariCP.getConnection(), false);
        operation.doSomething(dataSourceDBCP.getConnection(), false);
        operation.doSomething(dataSourceCp30.getConnection(), false);
        operation.doSomething(dataSourceTomcatJdbc.getConnection(), true);

    }
}
