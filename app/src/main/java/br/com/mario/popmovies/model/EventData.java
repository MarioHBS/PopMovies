package br.com.mario.popmovies.model;

/** Created by MarioH on 22/12/2016. */
public class EventData {
	private Integer movieID;
	private boolean isFavourite;

	public EventData(Integer movieID, boolean isFavourite) {
		this.movieID = movieID;
		this.isFavourite = isFavourite;
	}

	public int getMovieID() {
		return (movieID);
	}

	public boolean isFavourite() {
		return (isFavourite);
	}
}