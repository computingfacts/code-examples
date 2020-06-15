package com.computingfacts.pooling.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
class ApplicationOperationImpl implements ApplicationOperation {

    private final DataSource dataSource;

    public ApplicationOperationImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void doSomething(boolean cleanUp) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement stmt = connection.createStatement();
        log.info("creating a database table with connection " + connection.getCatalog());

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test (timer timestamp)");
        log.info("inserting data to created table ......");

        stmt.executeUpdate("INSERT INTO test VALUES (now())");

        ResultSet rs = stmt.executeQuery("SELECT timer FROM test");

        while (rs.next()) {

            log.info("Result : " + rs.getTimestamp("timer") + "\n");
        }

        if (cleanUp) {
            String sql = "DROP TABLE test ";
            log.info("deleting table ....");
            stmt.executeUpdate(sql);
        }
    }

}
