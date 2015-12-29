package me.quriosity15.moviefinder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import me.quriosity15.moviefinder.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public  static String TAG = "MainActivityFragment";
    public static int sortType = 0;
    private GridView mGridView;
    private static String apiKey = "--------------------ENTER API KEY HERE-----------------";
    private static String sortByPopularity = "popularity.desc";
    private static String sortByRating = "vote_average.desc";
    public static String moviePosition = "movie_position" ;
    MovieCursorAdapter movieCursorAdapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        this.setHasOptionsMenu(true);
        mGridView = (GridView) view.findViewById(R.id.grid_view);

        Cursor movieCursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        movieCursorAdapter = new MovieCursorAdapter(getActivity(), movieCursor, 0);
        mGridView.setAdapter(movieCursorAdapter);
//        movieCursor.close();
        gridUpdate();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(getActivity(), "" + position+id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(moviePosition,position);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v(TAG,"------------------------done-------------------");
        super.onCreateOptionsMenu(menu, inflater);
        Spinner s = (Spinner) menu.findItem(R.id.action_sort).getActionView();
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sortType = position;
                Log.v(TAG, Integer.toString(position));
                gridUpdate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public int gridUpdate(){
        String sortBy = "";
        if (sortType == 1 )
            sortBy = sortByRating;
        else
            sortBy = sortByPopularity;

        String urlString = new Uri.Builder()
                .scheme("http")
                .authority("api.themoviedb.org")
                .path("/3/discover/movie")
                .appendQueryParameter("sort_by", sortBy)
                .appendQueryParameter("api_key", apiKey)
                .build().toString();
        Log.v("Frag",urlString);

        URL url1;
        try {
            url1 = new URL(urlString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new DownloadFilesTask().execute(url1);
        return 1;
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            String response;
            response = Downloader.downloadFile(urls[0]);
            return response;
        }



        protected void onPostExecute(String result) {
            Log.v("DOWNLOAD","Downloaded " + result);
            final String RESULTS = "results";
            final String ENTRY_ID = "id";
            final String TITLE = "title";
            final String OVERVIEW = "overview";
            final String POSTER_URL = "poster_path";
            final String RELEASE_DATE = "release_date";
            final String USER_RATING = "vote_average";
            final String BACKGROUND_URL = "backdrop_path";

            try {
                JSONObject movieJson = new JSONObject(result);
                JSONArray movieArray = movieJson.getJSONArray(RESULTS);
                Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
                getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,null,null);

                for(int i = 0; i < movieArray.length(); i++) {
                    // These are the values that will be collected.
                    JSONObject movieEntry = movieArray.getJSONObject(i);
                    int movie_id = movieEntry.getInt(ENTRY_ID);
                    String title = movieEntry.getString(TITLE);
                    String overview = movieEntry.getString(OVERVIEW);
                    String url = movieEntry.getString(POSTER_URL);
                    String release_date = movieEntry.getString(RELEASE_DATE);
                    String user_rating = movieEntry.getString(USER_RATING);
                    String background_url = movieEntry.getString(BACKGROUND_URL);


                    ContentValues movieValues = new ContentValues();

                    movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_ENTRY_ID,movie_id );
                    movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, title);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_URL, url);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, overview);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, release_date);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_USER_RATING, user_rating);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_BACKGROUND_URL, background_url);

                    getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


//            ContentValues movieValues = new ContentValues();
//            movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_ENTRY_ID, "12");
//            movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, "The Martian");
//            movieValues.put(MovieContract.MovieEntry.COLUMN_NAME_URL, "qwertyyuii");
//            getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
            Cursor movieCursor = getActivity().getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

//            mGridView.setAdapter(new MovieCursorAdapter(getActivity(), movieCursor, 0));
            movieCursorAdapter.swapCursor(movieCursor);

            movieCursor.moveToFirst();
            if (movieCursor != null) {
                do {
                    for (int i = 0; i < movieCursor.getColumnCount(); i++) {

                        Log.v("LOG Database", movieCursor.getColumnName(i)+": " + movieCursor.getString(i));
                    }
                }while (movieCursor.moveToNext());
            }
            else {
                Log.e("mm", "No data" );

            }
//            movieCursor.close();


        }
    }
}