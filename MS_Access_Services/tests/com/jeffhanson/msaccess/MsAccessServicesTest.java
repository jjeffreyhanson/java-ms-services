package com.jeffhanson.msaccess;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Test;

public class MsAccessServicesTest
{
    @Test
    public void testGetInstance()
    {
        assertNotNull(MsAccessServices.getInstance());
    }

    @Test
    public void testConnect()
            throws Exception
    {
        MsAccessServices instance = MsAccessServices.getInstance();
        assertNotNull(instance);
        assertNotNull(instance.connect("Contacts.mdb", "", ""));
    }

    @Test
    public void testRead()
            throws Exception
    {
        MsAccessServices instance = MsAccessServices.getInstance();
        assertNotNull(instance);
        Connection conn = instance.connect("Contacts.mdb", "", "");
        assertNotNull(conn);
        Object[][] rows = instance.read(conn, "Contacts", new String[] { "*" }, "ContactId >= 2", true);
        assertTrue(rows != null & rows.length > 0);
    }
}
