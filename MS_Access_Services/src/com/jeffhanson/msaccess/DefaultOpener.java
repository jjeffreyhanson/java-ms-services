package com.jeffhanson.msaccess;

import java.io.File;
import java.io.IOException;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

import net.ucanaccess.jdbc.JackcessOpenerInterface;

public class DefaultOpener
    implements JackcessOpenerInterface
{

    @Override
    public Database open(File fl, String pwd) throws IOException
    {
        DatabaseBuilder dbd = new DatabaseBuilder(fl);

        // Note: the parameter setting autosync = true is recommended with UCanAccess for performance reasons.
        // UCanAccess flushes the updates to disk at transaction end.
        //
        // dbd.setAutoSync(false);
        // dbd.setReadOnly(false);

        return dbd.open();
    }
}
