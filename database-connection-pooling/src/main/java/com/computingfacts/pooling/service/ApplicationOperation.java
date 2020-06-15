package com.computingfacts.pooling.service;

import java.sql.SQLException;

/**
 *
 * @author joseph
 */
public interface ApplicationOperation {

    public void doSomething(boolean testMode) throws SQLException;
}
