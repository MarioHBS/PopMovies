package br.com.mario.popmovies;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import br.com.mario.popmovies.data.Movies;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Created by MarioH on 25/10/2016. */
class MovieViewHolder extends RecyclerView.ViewHolder {
	@Nullable
	@BindView(R.id.ivPoster)
	ImageView posterIv;
	@BindView(R.id.tvPosition)
	TextView positionTv;
	@BindView(R.id.tvRelease)
	TextView releaseTv;
	@BindView(R.id.tvRating)
	TextView ratingTv;

	MovieViewHolder(View itemView) {
		super(itemView);

		ButterKnife.bind(this, itemView);
	}

	void bind(Movies movie, int position) {
		if (posterIv != null)
			posterIv.setImageBitmap(movie.getPoster());

		positionTv.setText(position + 1 + "º");
		releaseTv.setText(movie.getReleaseDate());
		ratingTv.setText(String.format(Locale.getDefault(), "%.1f", movie.getAverage()));
	}
}