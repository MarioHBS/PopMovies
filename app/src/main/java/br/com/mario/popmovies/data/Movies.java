package br.com.mario.popmovies.data;

/** Created by MarioH on 01/11/2016. */
public class Movies {
	private final String title;
	private final String posterUrl;
	private final String releaseDate;
	private final String synopsis;
	private final double average;

	public Movies(String title, String posterUrl, String releaseDate, String synopsis, double
			  average) {
		this.title = title;
		this.posterUrl = posterUrl;
		this.releaseDate = releaseDate;
		this.synopsis = synopsis;
		this.average = average;
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
}