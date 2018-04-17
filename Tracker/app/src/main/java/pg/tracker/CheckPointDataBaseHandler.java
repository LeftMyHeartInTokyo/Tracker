package pg.tracker;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Osaka on 05.04.2018.
 */
public class CheckPointDataBaseHandler {
    private CheckPointHelper mDbHelper;
    private Context ctx;
    CheckPointDataBaseHandler(Context ctx) {
        this.ctx = ctx;
        this.mDbHelper = new CheckPointHelper(ctx);
        // Gets the data repository in write mode

    }

    public void writeData(String name, Double lat, Double lon) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CheckPointEntry.FeedEntry.COLUMN_NAME_NAME, name);
        values.put(CheckPointEntry.FeedEntry.COLUMN_NAME_LAT, lat);
        values.put(CheckPointEntry.FeedEntry.COLUMN_NAME_LON, lon);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(CheckPointEntry.FeedEntry.TABLE_NAME, null, values);
    }

    public void deleteData(String name, Double lat, Double lon){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(CheckPointEntry.FeedEntry.TABLE_NAME,
                CheckPointEntry.FeedEntry.COLUMN_NAME_NAME+"='"+name+"'"+" AND "+
                CheckPointEntry.FeedEntry.COLUMN_NAME_LAT+"='"+lat+"'"+" AND "+
                CheckPointEntry.FeedEntry.COLUMN_NAME_LON+"='"+lon+"'", null);

    }

    public void deleteAllData(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(CheckPointEntry.FeedEntry.TABLE_NAME,null, null);
    }

    public ArrayList<String> readData() {
        ArrayList<String> allItem = new ArrayList<>();

        String selectQuery = "SELECT * FROM "+ CheckPointEntry.FeedEntry.TABLE_NAME;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        Cursor cursor=database.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                String data="";
                data += cursor.getString(1) + ", ";//lat
                data += cursor.getDouble(2) + ", ";//lon
                data += cursor.getDouble(3);
                allItem.add(data);

            }while(cursor.moveToNext());
        }
        return allItem;

    }
}
