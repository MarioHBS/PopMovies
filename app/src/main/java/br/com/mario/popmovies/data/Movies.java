package br.com.mario.popmovies.data;

import android.graphics.Bitmap;

/** Created by MarioH on 01/11/2016. */
public class Movies {
	private final int id;
	private final String title;
	private final String posterUrl;
	private final String backdropUrl;
	private final String releaseDate;
	private final String synopsis;
	private final double average;

	private Bitmap poster;

	public Movies(int id, String title, String posterUrl, String backdropUrl, String releaseDate,
	              String synopsis, double average) {
		this.id = id;
		this.title = title;
		this.posterUrl = posterUrl;
		this.backdropUrl = backdropUrl;
		this.releaseDate = releaseDate;
		this.synopsis = synopsis;
		this.average = average;
	}

	public Bitmap getPoster() {
		return (poster);//(new BitmapDrawable(ctx.getResources(), bitmap));
	}

	public void setPoster(Bitmap poster) {
		this.poster = poster;
	}

	public String getBackdropUrl() {
		return (backdropUrl);
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

	public int getId() {
		return (id);
	}
}