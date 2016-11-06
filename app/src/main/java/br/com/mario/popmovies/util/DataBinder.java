package br.com.mario.popmovies.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.mario.popmovies.BuildConfig;

/** Created by MarioH on 01/11/2016. */
public class DataBinder {
	private static final String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";
	private static final String SIZE = "w185";
	private static final String APPID_PARAM = "api_key";

	private DataBinder() {
		//NO-OP
	}

	@BindingAdapter("imageUrl")
	public static void setImageUrl(ImageView imageView, String url) {
		Context context = imageView.getContext();

		Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
				  .appendPath(SIZE)
				  .appendEncodedPath(url)
				  .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY).build();


		URL url1 = null;
		try {
			url1 = new URL(builtUri.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Glide.with(context).load(url1).into(imageView);
	}
}