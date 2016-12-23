package br.com.mario.popmovies.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import br.com.mario.popmovies.PopMoviesApp;
import br.com.mario.popmovies.databinding.MovieItemBinding;
import br.com.mario.popmovies.model.Movies;
import br.com.mario.popmovies.screens.DetailActivity;

/** Created by MarioH on 25/10/2016. */
class MovieItemViewHolder extends RecyclerView.ViewHolder {
	private MovieItemBinding binding;

	MovieItemViewHolder(View itemView) {
		super(itemView);

		binding = DataBindingUtil.bind(itemView);
	}

	protected void bind(final Movies movie, int position) {
		movie.setPos(position + 1);

		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopMoviesApp.startActivity(DetailActivity.class, movie);
			}
		});

		binding.setMovie(movie);
	}
}