package br.com.mario.popmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.mario.popmovies.adapter.ReviewPageAdapter;
import br.com.mario.popmovies.databinding.ActivityDetailBinding;
import br.com.mario.popmovies.frag.ReviewFragment;
import br.com.mario.popmovies.util.GlobalConstants;

import static br.com.mario.popmovies.util.GlobalConstants.APPID_PARAM;
import static br.com.mario.popmovies.util.GlobalConstants.BACKDROP_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.MOVIE_ID_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.RELEASE_DATE_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.SYNOPSIS_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.VOTE_KEY;
import static br.com.mario.popmovies.util.MoviesDataParser.getReviewsDataFromJson;

/** Classe para exibir os detalhes de um filme selecionado */
public class DetailActivity extends AppCompatActivity {
	private static final String TMDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
	private static final String TMDB_REVIEWS_BASE_URL = "https://api.themoviedb.org/3/movie";
	private static final String SIZE = "w500"; // w92, w154, w185, w342, w500, w780, or original

	private ActivityDetailBinding binding;
	private ActionBar supportActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

		setSupportActionBar(binding.toolbar);
		supportActionBar = getSupportActionBar();

		if (supportActionBar != null)
			supportActionBar.setDisplayHomeAsUpEnabled(true);

		// ajuste do tamanho da CoordinatorLayout expandida para 3/5 da altura da tela
		AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
		float heightDp = getResources().getDisplayMetrics().heightPixels / 5;
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
		lp.height = (3 * (int) heightDp);
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		final Intent it = getIntent();

		int movieID = it.getIntExtra(MOVIE_ID_KEY, 0);
		String releaseDateStr = it.getStringExtra(RELEASE_DATE_KEY);
		String backExtraUrl = it.getStringExtra(BACKDROP_KEY);
		double voteDbl = it.getDoubleExtra(VOTE_KEY, 0);
		String synopsisTxt = it.getStringExtra(SYNOPSIS_KEY);

		// URI para recuperar a imagem de pôster
		Uri backdropUri = Uri.parse(TMDB_POSTER_BASE_URL).buildUpon()
				  .appendPath(SIZE)
				  .appendEncodedPath(backExtraUrl)
				  .build();

		// URI para recuperar os reviews de um filme
		Uri reviewUri = Uri.parse(TMDB_REVIEWS_BASE_URL).buildUpon()
				  .appendPath(String.valueOf(movieID))
				  .appendPath("reviews")
				  .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY)
				  .build();

		// atribuição dos valores aos componentes:
		Glide.with(DetailActivity.this).load(backdropUri).into(binding.backdropIv);
		supportActionBar.setTitle(it.getStringExtra(GlobalConstants.MOVIE_TITLE_KEY));
		binding.included.tvReleaseDate.setText(releaseDateStr);
		binding.included.tvRating.setText(String.valueOf(voteDbl));
		binding.included.tvSynopsis.setText(synopsisTxt);

		new ReviewAsync().execute(reviewUri);
	}

	private void setReviewComponents(ArrayList<String> strings) {
		ArrayList<Fragment> fragList = new ArrayList<>(strings.size());

		if (strings.size() > 0)
			for (int i = 0; i < strings.size(); i++)
				fragList.add(ReviewFragment.newInstance(strings.get(i)));
		else
			fragList.add(ReviewFragment.newInstance(null));

		ReviewPageAdapter mAdapter = new ReviewPageAdapter(getSupportFragmentManager(), fragList);
		binding.included.mReviewPager.setAdapter(mAdapter);
	}

	private class ReviewAsync extends AsyncTask<Uri, Void, ArrayList<String>> {
		@Override
		protected ArrayList<String> doInBackground(Uri... params) {
			if (params.length == 0)
				return (null);

			// These two need to be declared outside the try/catch
			// so that they can be closed in the finally block.
			HttpURLConnection urlConnection;
			BufferedReader reader;

			// Will contain the raw JSON response as a string.
			String reviewJsonStr;

			// Create the request to OpenWeatherMap, and open the connection
			try {
				URL url = new URL(params[0].toString());

				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setConnectTimeout(GlobalConstants.CONNECTION_TIME_LIMIT);
				urlConnection.setReadTimeout(GlobalConstants.CONNECTION_TIME_LIMIT);
				urlConnection.connect();

				// Read the input stream into a String
				InputStream inputStream = urlConnection.getInputStream();
				if (inputStream == null)
					return null;

				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				StringBuilder buffer = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
					// But it does make debugging a *lot* easier if you print out the completed
					// buffer for debugging.
					buffer.append(line).append("\n");
				}

				if (buffer.length() == 0) // Stream was empty.  No point in parsing.
					return (null);

				reviewJsonStr = buffer.toString();

				return (getReviewsDataFromJson(reviewJsonStr));

			} catch (IOException e) {
				e.printStackTrace();
				return (null);
			} catch (JSONException e) {
				e.printStackTrace();
				return (null);
			}
		} // end of doInBackground
		@Override
		protected void onPostExecute(ArrayList<String> strings) {
			if (strings != null)
				setReviewComponents(strings);
		}
	} // end of ReviewAsync
}