package cz.misak69.module_beta.impl;

import java.sql.SQLException;

/**
 * User: ${mjenicek}
 * Date: 2014.11.11
 */
public interface ISQLCall {

    public void getConnection() throws SQLException;

    public void closeEverything();

    public void getResultSet() throws SQLException;

    public void execute();
}
