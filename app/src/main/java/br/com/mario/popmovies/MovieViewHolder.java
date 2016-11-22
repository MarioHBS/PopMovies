package br.com.mario.popmovies;

import android.graphics.Typeface;
import android.os.Build;
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
	protected ImageView posterIv;
	@BindView(R.id.tvPosition)
	protected TextView positionTv;
	@BindView(R.id.tvRelease)
	protected TextView releaseTv;
	@BindView(R.id.tvRating)
	protected TextView ratingTv;

	MovieViewHolder(View itemView) {
		super(itemView);

		ButterKnife.bind(this, itemView);
	}

	void bind(final Movies movie, int position) {
		if (posterIv != null)
			posterIv.setImageBitmap(movie.getPoster());

		positionTv.setText(position + 1 + "º");
		releaseTv.setText(movie.getReleaseDate());
		ratingTv.setText(String.format(Locale.getDefault(), "%.1f", movie.getAverage()));

		// compatibilidade para versões 15 ou menores
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) { // 15
			positionTv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
			releaseTv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
			ratingTv.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
		}

		itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopMoviesApplication.startActivity(DetailActivity.class, movie);
			}
		});
	}
}