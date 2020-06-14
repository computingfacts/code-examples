
package com.computingfacts.pooling.service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author joseph
 */
public interface ApplicationOperation {
    
    public void doSomething(Connection connection, boolean testMode) throws SQLException;
}
