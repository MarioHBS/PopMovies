package br.com.mario.popmovies.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import br.com.mario.popmovies.data.Movies;

import static android.R.attr.bitmap;
import static android.media.CamcorderProfile.get;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/** Created by MarioH on 03/11/2016. */
public class MoviesDataParser {
	public static Movies[] getMovieDataFromJson(Context ctx, String tmdbJsonStr) throws
			  JSONException, MalformedURLException {
		// These are the names of the JSON objects that need to be extracted.
		final String MAIN = "results";
		final String POSTER = "poster_path";
		final String SYNOPSIS = "overview";
		final String DATE = "release_date";
		final String TITLE = "original_title";
		final String VOTE = "vote_average";

		JSONObject tmdbJson = new JSONObject(tmdbJsonStr);
		JSONArray movies = tmdbJson.getJSONArray(MAIN);

		int tam = movies.length();
		Movies[] listMovie = new Movies[tam];

		String baseUrl = "http://image.tmdb.org/t/p/w185/";

		for (int i = 0; i < tam; i++) {
			JSONObject movie = movies.getJSONObject(i);
			listMovie[i] = new Movies(movie.getString(TITLE), movie.getString(POSTER), movie.getString
					  (DATE), movie.getString(SYNOPSIS), movie.getDouble(VOTE));

			URL url = new URL(baseUrl + movie.getString(POSTER));

			try {
				listMovie[i].setPoster(Glide.with(ctx).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get());
//				listMovie[i].setPoster(new BitmapDrawable(ctx.getResources(), Glide.with(ctx).load(url).asBitmap().into(-1, -1).get()));

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			Log.d(TAG, "Movies names: " + movie.getString(TITLE));
		}

		return (listMovie);
	}
}