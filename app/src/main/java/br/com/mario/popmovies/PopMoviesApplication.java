package br.com.mario.popmovies;

import android.app.Application;
import android.content.Context;

/** Created by MarioH on 09/11/2016. */
public class PopMoviesApplication extends Application {
	protected static Context CTX;

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
	}


}