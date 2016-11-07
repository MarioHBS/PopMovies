package br.com.mario.popmovies.data;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/** Created by MarioH on 01/11/2016. */
public class Movies {
	private final String title;
	private final String posterUrl;
	private final String releaseDate;
	private final String synopsis;
	private final double average;

	private Bitmap poster;

	public Movies(String title, String posterUrl, String releaseDate, String synopsis, double
			  average) {
		this.title = title;
		this.posterUrl = posterUrl;
		this.releaseDate = releaseDate;
		this.synopsis = synopsis;
		this.average = average;
	}

	public Bitmap getPoster() {
		return (poster);//(new BitmapDrawable(ctx.getResources(), bitmap));
	}

	public double getAverage() {
		return average;
	}

	public String getPosterUrl() {
		return (posterUrl);
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public String getTitle() {
		return title;
	}

	public void setPoster(Bitmap poster) {
		this.poster = poster;
	}
}