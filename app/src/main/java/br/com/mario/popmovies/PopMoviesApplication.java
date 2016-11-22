package br.com.mario.popmovies;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import br.com.mario.popmovies.data.Movies;

import static br.com.mario.popmovies.util.GlobalConstants.BACKDROP_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.MOVIE_ID_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.MOVIE_TITLE_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.POSTER_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.RELEASE_DATE_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.SYNOPSIS_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.VOTE_KEY;

/** Created by MarioH on 09/11/2016. */
public class PopMoviesApplication extends Application {
	protected static Context CTX;
	private static PopMoviesApplication instance;

	public static PopMoviesApplication getInstance() {
		return (instance);
	}

	/**
	 * Called when the application is starting, before any activity, service,
	 * or receiver objects (excluding content providers) have been created.
	 * Implementations should be as quick as possible (for example using
	 * lazy initialization of state) since the time spent in this function
	 * directly impacts the performance of starting the first activity,
	 * service, or receiver in a process.
	 * If you override this method, be sure to call super.onCreate().
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		CTX = getApplicationContext();
		instance = this;
	}

	public static void startActivity(Class<?> aClass, Movies mv) {
		Intent it = new Intent(CTX, aClass);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Bitmap bmp = mv.getPoster();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		it.putExtra(MOVIE_ID_KEY, mv.getId());
		it.putExtra(MOVIE_TITLE_KEY, mv.getTitle());
		it.putExtra(POSTER_KEY, byteArray);
		it.putExtra(BACKDROP_KEY, mv.getBackdropUrl());
		it.putExtra(SYNOPSIS_KEY, mv.getSynopsis());
		it.putExtra(RELEASE_DATE_KEY, mv.getReleaseDate());
		it.putExtra(VOTE_KEY, mv.getAverage());

		getInstance().startActivity(it);
	}
}