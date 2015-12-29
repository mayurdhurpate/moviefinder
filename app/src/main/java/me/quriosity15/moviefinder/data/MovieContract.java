package me.quriosity15.moviefinder.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mayur on 3/12/15.
 */
public final class MovieContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public MovieContract() {
    }

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "me.quriosity15.moviefinder";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    /* Inner class that defines the table contents */
    public static abstract class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_URL = "imageurl";
        public static final String COLUMN_NAME_BACKGROUND_URL = "image_background_url";
        public static final String COLUMN_NAME_USER_RATING = "user_rating";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static String buildImageUrl(String url_tail)
        {
            final String size = "w185";
            return  new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .path("/t/p/" + size + url_tail)
                    .build().toString();
        }

        public static String buildImageUrl(String url_tail, String size)
        {
            return  new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .path("/t/p/" + size + url_tail)
                    .build().toString();
        }
    }
}
