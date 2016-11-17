package com.bewtechnologies.gtone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MAHE on 7/15/2015.
 */




public class LocationDBHelper extends SQLiteOpenHelper{

    //if database changes; update the db version

    public static final int DATABSE_VERSION=1;

    public static final String DATABASE_NAME="gtone.db";


    //create command for db





    public LocationDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABSE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LocationContract.LocationEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(LocationContract.LocationEntry.SQL_DELETE_ENTRIES);
    }
}
