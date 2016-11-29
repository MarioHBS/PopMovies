package br.com.mario.popmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.mario.popmovies.custom.CustomPager;
import br.com.mario.popmovies.frag.ReviewFragment;
import br.com.mario.popmovies.util.GlobalConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.mario.popmovies.util.GlobalConstants.APPID_PARAM;
import static br.com.mario.popmovies.util.GlobalConstants.BACKDROP_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.MOVIE_ID_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.RELEASE_DATE_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.SYNOPSIS_KEY;
import static br.com.mario.popmovies.util.GlobalConstants.VOTE_KEY;
import static br.com.mario.popmovies.util.MoviesDataParser.getReviewsDataFromJson;

public class DetailActivity extends AppCompatActivity {
	private static final String TMDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
	private static final String TMDB_REVIEWS_BASE_URL = "https://api.themoviedb.org/3/movie";
	private static final String SIZE = "w500"; // w92, w154, w185, w342, w500, w780, or original

	private ReviewPageAdapter mAdapter;

//	@BindView(R.id.tv_reviews_counter)
//	protected TextView reviewCounterTv;
	@BindView(R.id.pager_review)
	protected CustomPager mReviewPager;
//	@BindView(R.id.left_nav)
//	protected ImageButton leftNav;
//	@BindView(R.id.right_nav)
//	protected ImageButton rightNav;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		ButterKnife.bind(this);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		AppBarLayout appbar = (AppBarLayout) findViewById(R.id.app_bar);
		float heightDp = getResources().getDisplayMetrics().heightPixels / 5;
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
		lp.height = 3 * (int) heightDp;

		//		imageView.setImageBitmap(BitmapFactory.decodeByteArray(mPosterImageInByte, 0,
		// mPosterImageInByte.length));

//		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction
//						  ("Action", null).show();
//			}
//		});
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		ImageView imageView = ButterKnife.findById(this, R.id.image);
		TextView releaseTv = ButterKnife.findById(this, R.id.tv_release_date);
		TextView averageTv = ButterKnife.findById(this, R.id.tv_rating);
		TextView synopsisTv = ButterKnife.findById(this, R.id.tv_synopsis);

		final Intent it = getIntent();

		int movieID = it.getIntExtra(MOVIE_ID_KEY, 0);
		String releaseDateStr = it.getStringExtra(RELEASE_DATE_KEY);
		String backExtraUrl = it.getStringExtra(BACKDROP_KEY);
		double voteDbl = it.getDoubleExtra(VOTE_KEY, 0);
		String synopsisTxt = it.getStringExtra(SYNOPSIS_KEY);

		Uri backdropUri = Uri.parse(TMDB_POSTER_BASE_URL).buildUpon()
				  .appendPath(SIZE)
				  .appendEncodedPath(backExtraUrl)
				  .build();

		Uri reviewUri = Uri.parse(TMDB_REVIEWS_BASE_URL).buildUpon()
				  .appendPath(String.valueOf(movieID))
				  .appendPath("reviews")
				  .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY)
				  .build();

		// atribuição dos valores:
		Glide.with(DetailActivity.this).load(backdropUri).into(imageView);
		getSupportActionBar().setTitle(it.getStringExtra(GlobalConstants.MOVIE_TITLE_KEY));
		releaseTv.setText(releaseDateStr);
		averageTv.setText(String.valueOf(voteDbl));
		synopsisTv.setText(synopsisTxt);

		new ReviewAsync().execute(reviewUri);
	}

	private void setReviewComponents(ArrayList<String> strings) {
		ArrayList<Fragment> fragList = new ArrayList<>(strings.size());

		if (strings.size() > 0)
			for (int i = 0; i < strings.size(); i++)
				fragList.add(ReviewFragment.newInstance(strings.get(i)));
		else
			fragList.add(ReviewFragment.newInstance(null));

		mAdapter = new ReviewPageAdapter(getSupportFragmentManager(), fragList);
		mReviewPager.setAdapter(mAdapter);

		/*int tam = strings.size();

		if (tam == 0){
			leftNav.setVisibility(View.GONE);
			rightNav.setVisibility(View.GONE);
		} else {
			leftNav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int tab = mReviewPager.getCurrentItem();
					if (tab > 0) {
						tab--;
						mReviewPager.setCurrentItem(tab);
					} else if (tab == 0) {
						mReviewPager.setCurrentItem(tab);
					}
				}
			});

			// Images right navigatin
			rightNav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int tab = mReviewPager.getCurrentItem();
					tab++;
					mReviewPager.setCurrentItem(tab);
				}
			});
		}*/
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
					// Nothing to do.
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

				if (buffer.length() == 0)
					// Stream was empty.  No point in parsing.
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
			if (strings != null) {
//				if (strings.isEmpty())
//					reviewCounterTv.setText("0");
//				else {
					String str = "(" + 1 + "/" + strings.size() + ")";
//					reviewCounterTv.setText(str);

					setReviewComponents(strings);
//				}
			}
		}
	} // end of ReviewAsync
}