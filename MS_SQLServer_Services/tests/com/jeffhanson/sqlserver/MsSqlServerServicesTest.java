package com.jeffhanson.sqlserver;

import static org.junit.Assert.*;

import org.junit.Test;

public class MsSqlServerServicesTest
{
    @Test
    public void testGetInstance()
    {
        assertNotNull(MsSqlServerServices.getInstance());
    }
}
