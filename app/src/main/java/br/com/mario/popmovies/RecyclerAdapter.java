package br.com.mario.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.mario.popmovies.data.Movies;

import static android.R.id.list;

/** Created by MarioH on 25/10/2016. */
public class RecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {
	private final List<Movies> movies;

	private RecyclerAdapter(List<Movies> movies) {
		this.movies = movies;
	}

	public static RecyclerAdapter getInstance() {
		List<Movies> movies = new ArrayList<>();
		return (new RecyclerAdapter(movies));
	}

	@Override
	public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final Context ctx = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View statusContainer = inflater.inflate(R.layout.movie_item, parent, false);

//		statusContainer.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(ctx, "Clicado", Toast.LENGTH_SHORT).show();
//			}
//		});

		return (new MovieViewHolder(statusContainer));
	}

	@Override
	public void onBindViewHolder(MovieViewHolder holder, int position) {
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