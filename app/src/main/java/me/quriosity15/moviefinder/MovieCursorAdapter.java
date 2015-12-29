package me.quriosity15.moviefinder;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import me.quriosity15.moviefinder.data.MovieContract;

/**
 * Created by mayur on 4/12/15.
 */
public class MovieCursorAdapter extends CursorAdapter{

    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grid_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView poster = (ImageView) view.findViewById(R.id.poster);
//        poster.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String url_tail = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_URL));
//        final String size = "w185";
//        String urlString = new Uri.Builder()
//                .scheme("http")
//                .authority("image.tmdb.org")
//                .path("/t/p/" + size + url_tail)
//                .build().toString();
        String urlString = MovieContract.MovieEntry.buildImageUrl(url_tail);
        Picasso.with(context)
                .load(urlString)
                .into(poster);

    }
}
