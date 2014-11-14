package cz.misak69.module_beta.impl;

import java.sql.SQLException;

/**
 * User: ${mjenicek}
 * Date: 2014.11.11
 */
public class CStoredProcedure implements ISQLCall{


    public void registerOutParameter()throws SQLException {}

    public void setString() throws SQLException {}

    public void setInt() throws SQLException {}

    @Override
    public void execute(){}

    public void executeQuery(){}

    public void executeStringQuery(){}

    public void executeLongQuery(){}

    public void executeIntQuery(){}

    public void executeDateQuery(){}

    public void executeDatabaseQuery(){}

    public void executeStringQueryNoLogs(){}

    public void executeQueryWithoutLogs(){}

    public void xExecute(){}

    public void xExecuteQuery(){}

    @Override
    public void closeEverything(){}

    public void closeEverythingLogCursorCount(){}

    public void closeRegisteredCursors(){}

    @Override
    public void getConnection() throws SQLException {}

    @Override
    public void getResultSet() throws SQLException {}
}
