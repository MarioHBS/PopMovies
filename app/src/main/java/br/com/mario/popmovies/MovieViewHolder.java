package br.com.mario.popmovies;

import android.databinding.DataBindingUtil;
import android.databinding.tool.DataBindingBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import br.com.mario.popmovies.data.Movies;
import br.com.mario.popmovies.databinding.MovieItemBinding;
import butterknife.BindView;

/** Created by MarioH on 25/10/2016. */
public class MovieViewHolder extends RecyclerView.ViewHolder {
	private MovieItemBinding binding;

	public MovieViewHolder(View itemView) {
		super(itemView);
		binding = DataBindingUtil.bind(itemView);
	}

	public void bind(Movies movie) {
		binding.setMovie(movie);
	}
}