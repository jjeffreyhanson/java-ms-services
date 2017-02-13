package com.jeffhanson.msaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.ucanaccess.jdbc.UcanaccessDriver;

/**
 * This class provides methods for interfacing with MS Access files
 *
 * @author J. Jeffrey Hanson
 */
public class MsAccessServices
{
    private static MsAccessServices instance = new MsAccessServices();

    /**
     * Singleton instantiator
     * 
     * @return
     */
    public static MsAccessServices getInstance()
    {
        return instance;
    }

    /**
     * Constructor
     */
    private MsAccessServices()
    {
        // hide from direct instantiation
    }

    /**
     * Connect to an MS Access file
     * 
     * @param dataFileName
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public Connection connect(String dataFileName, String username, String password) throws Exception
    {
        String url = UcanaccessDriver.URL_PREFIX + dataFileName + ";jackcessOpener=com.jeffhanson.msaccess.DefaultOpener";

        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Close MS Access file connection
     * 
     * @param conn
     */
    public void close(Connection conn)
    {
        try
        {
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Create/insert MS Access file objects
     * 
     * @param conn
     * @param tableName
     * @param columnNames
     * @param columnValues
     * @return
     */
    public synchronized int create(Connection conn, String tableName, String columnNames, String columnValues)
    {
        // TODO
        return 0;
    }

    /**
     * Read MS Access file objects
     * 
     * @param conn
     * @param tableName
     * @param columns
     * @param whereClause
     * @param includeColNames
     * @return
     */
    public synchronized Object[][] read(Connection conn, String tableName, String[] columns, String whereClause, boolean includeColNames)
    {
        ArrayList<Object[]> rows = new ArrayList<Object[]>();

        Statement st = null;
        ResultSet rs = null;

        try
        {
            st = conn.createStatement();

            String columnStr = "*";

            if (columns[0].trim().equalsIgnoreCase("*") == false)
            {
                columnStr = StringUtils.join(columns, ",");
            }

            String sqlStr = "SELECT " + columnStr + " FROM " + tableName
                            + ((null != whereClause && whereClause.length() > 0) ? (" WHERE " + whereClause) : "");

            System.out.println("MS Access services read sqlStr [" + sqlStr + "]");

            rs = st.executeQuery(sqlStr);

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
                ArrayList<Object> rowColVals = new ArrayList<Object>();

                int columnCount = rs.getMetaData().getColumnCount();

                if (columns[0].trim().equalsIgnoreCase("*") == false)
                {
                    columnCount = columns.length;
                }

                for (int i = 1; i <= columnCount; ++i)
                {
                    Object obj = rs.getObject(i);
                    rowColVals.add(obj);
                }

                Object[] rowColValsArr = new Object[rowColVals.size()];

                rows.add(rowColVals.toArray(rowColValsArr));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (Exception e)
                {
                }
            }
            if (st != null)
            {
                try
                {
                    st.close();
                }
                catch (Exception e)
                {
                }
            }
        }

        Object[][] rowsArr = new Object[rows.size()][];

        return rows.toArray(rowsArr);
    }

    /**
     * Update MS Access file objects
     * 
     * @param conn
     * @param tableName
     * @param colNameValMap
     * @param whereClause
     * @return
     */
    public synchronized int update(Connection conn, String tableName, Map<String, Object> colNameValMap, String whereClause)
    {
        int updateCount = 0;

        PreparedStatement ps = null;

        try
        {
            String colValStr = "";
            ArrayList<Object> values = new ArrayList<Object>();

            Iterator<String> keyIter = colNameValMap.keySet().iterator();

            while (keyIter.hasNext())
            {
                if (colValStr.length() > 0)
                {
                    colValStr += ", ";
                }

                String key = keyIter.next();

                colValStr += (key + "= ?");

                values.add(colNameValMap.get(key));
            }

            ps = conn.prepareStatement("UPDATE " + tableName + " SET " + colValStr
                                       + ((null != whereClause && whereClause.length() > 0) ? (" WHERE " + whereClause) : ""));

            for (int i = 0; i < values.size(); i++)
            {
                ps.setObject(i + 1, values.get(i));
            }

            updateCount = ps.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ps != null)
            {
                try
                {
                    ps.close();
                }
                catch (Exception e)
                {
                }
            }
        }

        return updateCount;
    }

    /**
     * Delete MS Access file objects
     * 
     * @param conn
     * @param tableName
     * @return
     */
    public synchronized int delete(Connection conn, String tableName)
    {
        // TODO
        return 0;
    }
}
