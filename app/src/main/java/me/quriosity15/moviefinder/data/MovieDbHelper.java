package me.quriosity15.moviefinder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import me.quriosity15.moviefinder.data.MovieContract.MovieEntry;

/**
 * Created by mayur on 4/12/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;

    static final String DATABASE_NAME = "weather.db";
    
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_NAME_ENTRY_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_NAME_URL + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_NAME_BACKGROUND_URL + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_NAME_USER_RATING + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_NAME_RELEASE_DATE + " DATE" +
                " );";
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
