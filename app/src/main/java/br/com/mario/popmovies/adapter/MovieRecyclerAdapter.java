package br.com.mario.popmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.mario.popmovies.R;
import br.com.mario.popmovies.data.Movies;

/** Created by MarioH on 25/10/2016. */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {
	private final List<Movies> movies;

	private MovieRecyclerAdapter(List<Movies> movies) {
		this.movies = movies;
	}

	public static MovieRecyclerAdapter getInstance() {
		List<Movies> movies = new ArrayList<>();
		return (new MovieRecyclerAdapter(movies));
	}

	@Override
	public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final Context ctx = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View statusContainer = inflater.inflate(R.layout.movie_item, parent, false);

		return (new MovieItemViewHolder(statusContainer));
	}

	@Override
	public void onBindViewHolder(MovieItemViewHolder holder, int position) {
		Movies movie = this.movies.get(position);
		holder.bind(movie, position);
	}

	@Override
	public int getItemCount() {
		return (movies.size());
	}

	public void setMovies(int page, List<Movies> movies) {
		if (page == 1)
			this.movies.clear(); // ??
		this.movies.addAll(movies);
		notifyDataSetChanged();
	}

	public List<Movies> getList() {
		return (this.movies);
	}
}