package br.com.mario.popmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/** Created by MarioH on 19/11/2016. */
public class Utilities {
	public static boolean isNetworkActivated(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context
				  .CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		return (netInfo != null && netInfo.isConnectedOrConnecting());
	}

	public static boolean isOnline() {
		Runtime runtime = Runtime.getRuntime();
		try {

			Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
			int exitValue = ipProcess.waitFor();

			return (exitValue == 0);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		return (false);
	}
}