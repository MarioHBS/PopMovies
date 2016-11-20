package br.com.mario.popmovies;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import br.com.mario.popmovies.util.GlobalConstants;

import static br.com.mario.popmovies.util.GlobalConstants.BACKDROP_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.POSTER_KEY;

public class DetailActivity extends AppCompatActivity {
	private static final String TMDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
	private static final String SIZE = "w500"; // w92, w154, w185, w342, w500, w780, or original

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
		float heightDp = getResources().getDisplayMetrics().heightPixels / 5;
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
		lp.height = 3 * (int) heightDp;

		ImageView imageView = (ImageView) findViewById(R.id.image);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		final Intent it = getIntent();

		String backExtraUrl = it.getStringExtra(BACKDROP_KEY);

		Uri builtUri = Uri.parse(TMDB_POSTER_BASE_URL).buildUpon()
				  .appendPath(SIZE)
				  .appendEncodedPath(backExtraUrl)
				  .build();

		Glide.with(getBaseContext()).load(builtUri).into(imageView);

		getSupportActionBar().setTitle(it.getStringExtra(GlobalConstants.MOVIE_TITLE_KEY));
		//		imageView.setImageBitmap(BitmapFactory.decodeByteArray(mPosterImageInByte, 0,
		// mPosterImageInByte.length));

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction
						  ("Action", null).show();
			}
		});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
