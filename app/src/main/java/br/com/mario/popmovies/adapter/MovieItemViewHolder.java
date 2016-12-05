package br.com.mario.popmovies.adapter;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import br.com.mario.popmovies.DetailActivity;
import br.com.mario.popmovies.PopMoviesApplication;
import br.com.mario.popmovies.data.Movies;
import br.com.mario.popmovies.databinding.MovieItemBinding;

/** Created by MarioH on 25/10/2016. */
class MovieItemViewHolder extends RecyclerView.ViewHolder {
	private MovieItemBinding binding;

	MovieItemViewHolder(View itemView) {
		super(itemView);

		binding = DataBindingUtil.bind(itemView);
	}

	protected void bind(final Movies movie, int position) {
		//		if (posterIv != null)
		//			binding.ivPoster.setImageBitmap(movie.getPoster());
		movie.setPos(position + 1);

		//	binding.tvPosition.setText(position + 1 + "º");
		//	binding.tvRating.setText(String.format(Locale.getDefault(), "%.1f", movie.getAverage()));

		// compatibilidade para versões 15 ou menores
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			binding.tvPosition.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
			binding.tvRating.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
		}

		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopMoviesApplication.startActivity(DetailActivity.class, movie);
			}
		});

		binding.setMovie(movie);
	}
}