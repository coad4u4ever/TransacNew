package com.example.coad4u4ever.transacnew;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "transacDB";
    private static final int DB_VERSION = 1;
    private static final String DB_CREATE = "" +
            "CREATE TABLE transac (" +
            "id INTEGER PRIMARY KEY autoincrement, " +
            "detail TEXT NOT NULL, " +
            "amount REAL NOT NULL, " +
            "type TEXT NOT NULL, " +
            "date INTEGER NOT NULL, " +
            "balance REAL );";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(),
                "Upgread database version from version" + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS transac");
        onCreate(db);
    }
}
