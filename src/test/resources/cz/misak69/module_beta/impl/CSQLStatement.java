package cz.misak69.module_beta.impl;

import java.sql.SQLException;

/**
 * User: ${mjenicek}
 * Date: 2014.11.11
 */
public class CSQLStatement implements ISQLCall{

    @Override
    public void execute(){}

    public void executeDateQuery(){}

    public void executeDoubleQuery(){}

    public void executeIntQuery(){}

    public void executeLongQuery(){}

    public void executeQuery(){}

    public void executeQueryToMaps(){}

    public void executeQueryToSingleVector(){}

    public void executeStringQuery(){}

    @Override
    public void closeEverything(){}

    @Override
    public void getResultSet() throws SQLException {}

    @Override
    public void getConnection() throws SQLException {}
}
