package com.gtb.crypt.UTIL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by grove on 09/01/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="canales.sqlite";
    private static final int DB_SCHEME_VERSION =1;

    public DBHelper(Context context) {
        super(context, DB_NAME,null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
