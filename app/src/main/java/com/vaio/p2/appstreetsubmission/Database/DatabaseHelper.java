package com.vaio.p2.appstreetsubmission.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vaio.p2.appstreetsubmission.Utilities.Constants;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME , null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + Constants.PATH_TABLE + "(" + Constants.KEYWORD + " VARCHAR ,"
                + Constants.URL + " VARCHAR " + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.PATH_TABLE);
    }

    public long createImage(String keyword, String url) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEYWORD, keyword);
        values.put(Constants.URL, url);

        // insert row
        long insert_id = db.insert(Constants.PATH_TABLE, null, values);
        return insert_id;
    }

    public ArrayList<String> getAllImages(String keyword) {
        ArrayList<String> urls = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + Constants.PATH_TABLE
                + " WHERE "+Constants.KEYWORD + " = '" + keyword +"' ;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                urls.add(c.getString(c.getColumnIndex(Constants.URL)));
            } while (c.moveToNext());
        }

        return urls;
    }


}
