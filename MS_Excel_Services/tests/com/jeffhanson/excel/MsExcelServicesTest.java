package com.jeffhanson.excel;

import static org.junit.Assert.*;

import org.junit.Test;

public class MsExcelServicesTest
{
    @Test
    public void testGetInstance()
    {
        assertNotNull(MsExcelServices.getInstance());
    }

    @Test
    public void testReadAll()
            throws Exception
    {
        MsExcelServices instance = MsExcelServices.getInstance();
        assertNotNull(instance);
        Object[][] allRows = instance.readAll("test.xlsx", true);
        assertTrue(allRows != null && allRows.length > 0);
    }
}
