package com.computingfacts.pooling.dataconfig;

import com.computingfacts.pooling.config.ConfigProperties;
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
@SpringJUnitConfig({ ConfigProperties.class, PoolConfig.class})
public class PoolConfigTest {

    @Autowired
    private DataSource dataSourceHikariCP;
    @Autowired
    private DataSource dataSourceDBCP;
    @Autowired
    private DataSource dataSourceCp30;
    @Autowired
    private DataSource dataSourceTomcatJdbc;


    /**
     * Test of dataSourceCp30 method, of class PoolConfig.
     */
    @Test
    public void testDataSourceCp30() {

        assertThat(dataSourceCp30).isNotNull();

        assertThat(dataSourceCp30).isInstanceOf(ComboPooledDataSource.class);

    }

    /**
     * Test of dataSourceDBCP method, of class PoolConfig.
     */
    @Test
    public void testDataSourceDBCP2() {

        assertThat(dataSourceDBCP).isNotNull();

        assertThat(dataSourceDBCP).isInstanceOf(BasicDataSource.class);

    }

    /**
     * Test of dataSourceHikariCP method, of class PoolConfig.
     */
    @Test
    public void testDataSourceHikariCP() {
        assertThat(dataSourceHikariCP).isNotNull();

        assertThat(dataSourceHikariCP).isInstanceOf(HikariDataSource.class);

    }

    /**
     * Test of dataSourceTomcatJdbc method, of class PoolConfig.
     * @throws java.sql.SQLException
     */
    @Test
    public void testDataSourceTomcatJdbc() throws SQLException {

        assertTrue(dataSourceTomcatJdbc.getConnection().isValid(1));

        assertThat(dataSourceTomcatJdbc).isInstanceOf(org.apache.tomcat.jdbc.pool.DataSource.class);

    }

}
