package br.com.mario.popmovies.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.mario.popmovies.data.Movies;

import static android.media.CamcorderProfile.get;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/** Created by MarioH on 03/11/2016. */
public class MoviesDataParser {
	public static Movies[] getMovieDataFromJson(String tmdbJsonStr) throws JSONException {
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

		for (int i = 0; i < tam; i++) {
			JSONObject movie = movies.getJSONObject(i);
			listMovie[i] = new Movies(movie.getString(TITLE), movie.getString(POSTER), movie.getString
					  (DATE), movie.getString(SYNOPSIS), movie.getDouble(VOTE));

			Log.d(TAG, "Movies names: " + movie.getString(TITLE));
		}

		return (listMovie);
	}
}