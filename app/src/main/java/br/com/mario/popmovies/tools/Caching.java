/**
 * Based on this class:
 * https://github.com/Protino/Fad-Flicks/blob/master/app/src/main/java/com/calgen/prodek/fadflicks
 * /utils/Cache.java
 */
package br.com.mario.popmovies.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.mario.popmovies.R;
import br.com.mario.popmovies.model.Movies;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/** Created by MarioH on 12/12/2016. */
public class Caching {
	/**
	 * @param ctx Context needed to fetch DefaultSharedPreferences
	 * @param mv  Data that is supposed to be cached.
	 */
	public static void saveCacheData(Context ctx, Movies mv) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

		Gson gson = new Gson();
		String data = gson.toJson(mv);

		String key = ctx.getString(R.string.fav_prefs_key) + mv.getId(); // exclusive key
		sharedPreferencesEditor.putString(key, data);
		sharedPreferencesEditor.apply();
	}

	/**
	 * @param ctx {@link Context} needed to fetch DefaultSharedPreferences
	 * @param id  Integer to provide the id for the key
	 * @return String data is returned,{@code null} if not found.
	 */
	public static Movies getMovieCacheData(Context ctx, int id) {
		Movies movie;

		SharedPreferences sharedPreferences = getDefaultSharedPreferences(ctx);
		String key = ctx.getString(R.string.fav_prefs_key) + id;

		if (sharedPreferences.contains(key)) {
			String jsonStr = sharedPreferences.getString(key, null);
			movie = new Gson().fromJson(jsonStr, Movies.class);

			return (movie);
		}

		return (null);
	}

	// SparseBooleanArray

	/**
	 * @param ctx         {@link Context} needed to fetch DefaultSharedPreferences.
	 * @param id          id of movie whose favourite needs to be cached.
	 * @param isFavourite favourite value.
	 */
	public static void setFavouriteMovie(Context ctx, int id) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

//		SparseBooleanArray map = getFavouriteMovies(ctx);
		List<Integer> list = getFavouriteMovies(ctx);
		if (list == null)
			list = new ArrayList<>();
		list.add(id);

		Gson gson = new Gson();
		String data = gson.toJson(list);
		sharedPreferencesEditor.putString(ctx.getResources().getString(R.string.fav_movies_prefs_key), data);
		sharedPreferencesEditor.apply();
	}

	/**
	 * @param ctx {@link Context} needed to fetch DefaultSharedPreferences
	 * @return {@code HashMap<Integer, Boolean>} of favourite movies.
	 */
	public static ArrayList<Integer> getFavouriteMovies(Context ctx) {
		String data;
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(ctx);
		if (sharedPreferences.contains(ctx.getResources().getString(R.string.fav_movies_prefs_key))) {
			data = sharedPreferences.getString(ctx.getResources().getString(R.string
					  .fav_movies_prefs_key), null);
			if (data == null)
				return (null);

			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<Integer>>() {}.getType();

			return (gson.fromJson(data, type));
		}

		return (null);
	}
}