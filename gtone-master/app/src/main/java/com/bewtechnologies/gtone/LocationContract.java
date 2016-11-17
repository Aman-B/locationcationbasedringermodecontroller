package com.bewtechnologies.gtone;

import android.provider.BaseColumns;

/**
 * Created by MAHE on 7/15/2015.
 */
public final class LocationContract {

    /**
     * To avoid accidental instantiation of this class, provided an empty constructor
     */

    public LocationContract() {
    }

    /* Inner class that defines table contents */

    public static abstract class LocationEntry implements BaseColumns {

        public static final String TABLE_NAME = "location";

        public static final String COLUMN_NAME_PLACE_ID = "placeid";

        public static final String COLUMN_NAME_PLACE_NAME = "placename";

        public static final String COLUMN_NAME_PLACE_LAT = "lat";

        public static final String COLUMN_NAME_PLACE_LONG = "long";

        public static final String COLUMN_NAME_SETTING="setting";

        public static final String TEXT_TYPE = " TEXT";
        public static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                        LocationEntry._ID + " INTEGER PRIMARY KEY," +
                        LocationEntry.COLUMN_NAME_PLACE_ID + " LONG"+ COMMA_SEP +
                        LocationEntry.COLUMN_NAME_PLACE_NAME + TEXT_TYPE + COMMA_SEP +
                        LocationEntry.COLUMN_NAME_PLACE_LAT + " DOUBLE" + COMMA_SEP +
                        LocationEntry.COLUMN_NAME_PLACE_LONG + " DOUBLE" + COMMA_SEP +
                        LocationEntry.COLUMN_NAME_SETTING    + TEXT_TYPE +
                        ");";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;



    }
}