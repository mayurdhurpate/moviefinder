package me.quriosity15.moviefinder;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.quriosity15.moviefinder.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String movieBackgroundUrl;
    private  String movieUrl;
    private String movieName;
    private TextView ranking;
    private TextView userRating;
    private TextView releaseDate;
    private TextView description;
    private ImageView movieImage;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ranking = (TextView) view.findViewById(R.id.ranking);
        userRating = (TextView) view.findViewById(R.id.user_rating);
        releaseDate = (TextView) view.findViewById(R.id.release_date);
        description = (TextView) view.findViewById(R.id.description);
        movieImage = (ImageView) view.findViewById(R.id.movie_image);
        Intent intent = getActivity().getIntent();
        int position = intent.getIntExtra(MainActivityFragment.moviePosition, 0);
        Cursor movieCursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        movieCursor.moveToPosition(position);
        movieName = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_TITLE));
        String urlTail = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_BACKGROUND_URL));
        movieBackgroundUrl = MovieContract.MovieEntry.buildImageUrl(urlTail,"w342");
        String position_str = Integer.toString(position+1);
        ranking.setText("#"+position_str);
        userRating.setText("User Ratings: " + movieCursor.getString(movieCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_USER_RATING)));
        releaseDate.setText("Release Date: " + movieCursor.getString(movieCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE)));
        description.setText(movieCursor.getString(movieCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW)));
        urlTail = movieCursor.getString(movieCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_URL));
        movieUrl =  MovieContract.MovieEntry.buildImageUrl(urlTail);

        Picasso.with(getActivity()).load(movieUrl).into(movieImage);
        Log.v(MainActivityFragment.moviePosition, movieBackgroundUrl);
        movieCursor.close();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(movieName);
        ImageView header = (ImageView) getActivity().findViewById(R.id.header);
        Picasso.with(getActivity()).load(movieBackgroundUrl).into(header);

    }
}
