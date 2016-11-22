package br.com.mario.popmovies.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.mario.popmovies.data.Movies;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/** Created by MarioH on 03/11/2016. */
public class MoviesDataParser {
	private static final String TMDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
	private static final String SIZE = "w185";

	public static Movies[] getMovieDataFromJson(Context ctx, String tmdbJsonStr) throws
			  JSONException, MalformedURLException {
		// These are the names of the JSON objects that need to be extracted.
		final String MAIN = "results";
		final String POSTER = "poster_path";
		final String BACKDROP = "backdrop_path";
		final String SYNOPSIS = "overview";
		final String DATE = "release_date";
		final String TITLE = "original_title";
		final String VOTE = "vote_average";
		final String ID = "id";

		JSONObject tmdbJson = new JSONObject(tmdbJsonStr);
		JSONArray movies = tmdbJson.getJSONArray(MAIN);

		int tam = movies.length();
		Movies[] listMovie = new Movies[tam];

		for (int i = 0; i < tam; i++) {
			JSONObject movie = movies.getJSONObject(i);
			listMovie[i] = new Movies(movie.getInt(ID), movie.getString(TITLE), movie.getString
					  (POSTER), movie.getString(BACKDROP), movie.getString(DATE), movie.getString
					  (SYNOPSIS), movie.getDouble(VOTE));

			Uri builtUri = Uri.parse(TMDB_POSTER_BASE_URL).buildUpon()
					  .appendPath(SIZE) //
					  // "http://image.tmdb.org/t/p/w185/"
					  .appendEncodedPath(movie.getString(POSTER))
					  .build();

			try {
				listMovie[i].setPoster(Glide.with(ctx).load(builtUri).asBitmap().into(Target
						  .SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get());
				//				listMovie[i].setPoster(new BitmapDrawable(ctx.getResources(), Glide.with
				// (ctx).load(url).asBitmap().into(-1, -1).get()));

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				Log.e(TAG, "getMovieDataFromJson: " + e.getCause(), e);
				// TODO Tratar: Request failed 404: Not Found
				e.printStackTrace();
			}

			Log.d(TAG, "Movies names: " + movie.getString(TITLE));
		}

		return (listMovie);
	}

	public static ArrayList<String> getReviewsDataFromJson(String jsonStr) throws JSONException {
		// These are the names of the JSON objects that need to be extracted.
		final String MAIN = "results";
		final String REVIEW_CONTENT = "content";

		JSONObject tmdbJson = new JSONObject(jsonStr);
		JSONArray reviewArray = tmdbJson.getJSONArray(MAIN);

		int tam = reviewArray.length();
		ArrayList<String> list = new ArrayList<>(tam);

		for (int i = 0; i < tam; i++) {
			JSONObject review = reviewArray.getJSONObject(i);
			list.add(review.getString(REVIEW_CONTENT));
		}

		return (list);
	}
}