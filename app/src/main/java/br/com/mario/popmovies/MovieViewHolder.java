package br.com.mario.popmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.mario.popmovies.data.Movies;

/** Created by MarioH on 25/10/2016. */
public class MovieViewHolder extends RecyclerView.ViewHolder {
	private ImageView posterIv;

	public MovieViewHolder(View itemView) {
		super(itemView);

		posterIv = (ImageView) itemView.findViewById(R.id.ivPoster);
	}

	public void bind(Movies movie) {
		posterIv.setImageBitmap(movie.getPoster());
	}
}