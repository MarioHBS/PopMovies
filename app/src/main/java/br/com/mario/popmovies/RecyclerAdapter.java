package br.com.mario.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.mario.popmovies.data.Movies;

/** Created by MarioH on 25/10/2016. */
class RecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {
	private final List<Movies> movies;

	public RecyclerAdapter(List<Movies> movies) {
		this.movies = movies;
	}

	static RecyclerAdapter getInstance() {
		List<Movies> movies = new ArrayList<>();
		return (new RecyclerAdapter(movies));
	}

	@Override
	public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context ctx = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View statusContainer = inflater.inflate(R.layout.movie_item, parent, false);

		return (new MovieViewHolder(statusContainer));
	}

	@Override
	public void onBindViewHolder(MovieViewHolder holder, int position) {
		Movies movie = this.movies.get(position);
		holder.bind(movie);
	}

	@Override
	public int getItemCount() {
		return (movies.size());
	}

	public void setMovies(List<Movies> movies) {
		this.movies.clear();
		this.movies.addAll(movies);
		notifyDataSetChanged();
	}
}