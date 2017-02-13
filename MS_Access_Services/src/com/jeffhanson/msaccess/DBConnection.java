package com.jeffhanson.msaccess;

public class DBConnection
{
    private Object db = null;

    /**
     * Constructor
     * 
     * @param db
     */
    DBConnection(Object db)
    {
        this.db = db;
    }

    /**
     * Retrieve the DB object
     * @return
     */
    Object getDB()
    {
        return db;
    }
}
