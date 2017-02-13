package com.jeffhanson.sqlserver;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class provides methods for interfacing with MS SQL Server databases
 *
 * @author J. Jeffrey Hanson
 */
public class MsSqlServerServices
{
    private static final String JDBC_CONN_PREFIX = "jdbc:sqlserver://";
    private static final String DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static MsSqlServerServices instance = new MsSqlServerServices();

    /**
     * Singleton instantiator
     * 
     * @return
     */
    public static MsSqlServerServices getInstance()
    {
        return instance;
    }

    /**
     * Constructor
     */
    private MsSqlServerServices()
    {
        // hide from direct instantiation
    }

    /**
     * Connect to database
     * 
     * @param serverName
     * @param dbName
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public Connection connect(String serverName, String dbName, String userName, String password) throws Exception
    {
        Class.forName(DRIVER_NAME).newInstance();

        String url = JDBC_CONN_PREFIX + serverName + ";DatabaseName=" + dbName;

        Connection con = DriverManager.getConnection(url, userName, password);

        return con;
    }

    /**
     * Close database connection
     * 
     * @param con
     */
    public void close(Connection con)
    {
        try
        {
            if (null != con && con.isClosed() == false)
                con.close();
        }
        catch (Exception e)
        {
        }
    }

    /**
     * Create/insert database objects
     * 
     * @param con
     * @param tableName
     * @param columnNames
     * @param columnValues
     * @return
     */
    public synchronized int create(Connection con, String tableName, String columnNames, String columnValues)
    {
        int updateCount = 0;
        Statement stmt = null;

        try
        {
            String sqlStr = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + columnValues + ")";

            System.out.println(this.getClass().getName() + ".create() sql = " + sqlStr);

            stmt = con.createStatement();
            updateCount = stmt.executeUpdate(sqlStr);
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (null != stmt)
                    stmt.close();
            }
            catch (Exception e)
            {
            }
        }

        return updateCount;
    }

    /**
     * Read database objects
     * 
     * @param con
     * @param colNames
     * @param tableName
     * @param whereClause
     * @param includeColNames
     * @return
     */
    public synchronized Object[][] read(Connection con, String colNames, String tableName, String whereClause, boolean includeColNames)
    {
        ArrayList<Object[]> rows = new ArrayList<Object[]>();
        Statement stmt = null;
        ResultSet rs = null;

        try
        {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT " + colNames + " FROM " + tableName
                                   + ((null != whereClause && whereClause.length() > 0) ? (" WHERE " + whereClause) : ""));

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int iNumCols = resultSetMetaData.getColumnCount();

            if (true == includeColNames)
            {
                ArrayList<Object> row = new ArrayList<Object>();

                for (int i = 1; i <= iNumCols; i++)
                {
                    // String colType = resultSetMetaData.getColumnTypeName(i);
                    row.add(resultSetMetaData.getColumnLabel(i));
                }

                Object[] rowArr = new Object[row.size()];

                rows.add(row.toArray(rowArr));
            }

            while (rs.next())
            {
                ArrayList<Object> row = new ArrayList<Object>();

                for (int i = 1; i <= iNumCols; i++)
                {
                    row.add(rs.getObject(i));
                }

                Object[] rowArr = new Object[row.size()];

                rows.add(row.toArray(rowArr));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (null != rs)
                    rs.close();
            }
            catch (Exception e)
            {
            }
            try
            {
                if (null != stmt)
                    stmt.close();
            }
            catch (Exception e)
            {
            }
        }

        Object[][] rowArr = new Object[rows.size()][];

        return rows.toArray(rowArr);
    }

    /**
     * Update database objects
     * 
     * @param con
     * @param tableName
     * @param values
     * @param whereClause
     * @return
     */
    public synchronized int update(Connection con, String tableName, String values, String whereClause)
    {
        int updateCount = 0;
        Statement stmt = null;

        try
        {
            stmt = con.createStatement();
            updateCount = stmt.executeUpdate("UPDATE " + tableName + " SET " + values
                                             + ((null != whereClause && whereClause.length() > 0) ? (" WHERE " + whereClause) : ""));
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (null != stmt)
                    stmt.close();
            }
            catch (Exception e)
            {
            }
        }

        return updateCount;
    }

    /**
     * Delete database objects
     * 
     * @param con
     * @param tableName
     * @param whereClause
     * @return
     */
    public synchronized int delete(Connection con, String tableName, String whereClause)
    {
        int updateCount = 0;
        Statement stmt = null;

        try
        {
            stmt = con.createStatement();
            updateCount = stmt.executeUpdate("DELETE FROM " + tableName
                                             + ((null != whereClause && whereClause.length() > 0) ? (" WHERE " + whereClause) : ""));
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (null != stmt)
                    stmt.close();
            }
            catch (Exception e)
            {
            }
        }

        return updateCount;
    }
}
