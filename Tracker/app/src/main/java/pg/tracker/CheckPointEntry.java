package pg.tracker;

import android.provider.BaseColumns;

/**
 * Created by Osaka on 05.04.2018.
 */

public final class CheckPointEntry {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CheckPointEntry() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String  COLUMN_NAME_LAT = "lat";
        public static final String  COLUMN_NAME_LON = "lon";
    }
}

