package br.com.mario.popmovies;

import android.app.SearchManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.mario.popmovies.adapter.MovieRecyclerAdapter;
import br.com.mario.popmovies.data.Movies;
import br.com.mario.popmovies.databinding.TabFragPageBinding;
import br.com.mario.popmovies.util.ItemDecoration;
import br.com.mario.popmovies.util.MoviesDataParser;

public class SearchActivity extends AppCompatActivity {
	private static final String TAG = SearchActivity.class.getSimpleName();
	private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_state";
	/** limite do número de páginas carregadas */
	private static final int PAGE_LIMIT = 3;
	private static final int visibleThreshold = 4;

	/** número de colunas do Grid */
	private final int SPAN_COUNT = 2;
	private final String key = "array-movie";

	private MovieRecyclerAdapter mAdapter;
	private GridLayoutManager mLayoutManager;

	private TabFragPageBinding binding;

	private int firstVisibleItem, visibleItemCount, totalItemCount;
	private int previousTotal = 0;
	private int page = 1, pageCount = 1;
	private boolean loading = true;
	private String mQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.tab_frag_page);

		mAdapter = MovieRecyclerAdapter.getInstance();
		if (savedInstanceState != null) {
			ArrayList<Movies> list = savedInstanceState.getParcelableArrayList(key);
			mAdapter.setMovies(1, list);
		}
		mLayoutManager = new GridLayoutManager(SearchActivity.this, SPAN_COUNT);
		binding.listGridMovies.setLayoutManager(mLayoutManager);

		binding.listGridMovies.setEmptyView(binding.emptyTv);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		handleIntent(intent);
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		if (savedInstanceState == null)
			new SearchMovie().execute(mQuery);
		else {
			binding.progressSearch.setVisibility(View.GONE);
			Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable
					  (BUNDLE_RECYCLER_LAYOUT);
			binding.listGridMovies.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
		}

		binding.listGridMovies.addItemDecoration(new ItemDecoration(10, SPAN_COUNT));
		binding.listGridMovies.setAdapter(mAdapter);

		binding.listGridMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				if (dy > 0) {
					visibleItemCount = binding.listGridMovies.getChildCount();
					totalItemCount = mLayoutManager.getItemCount();
					firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

					loadMovieList();
				}
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mLayoutManager.onSaveInstanceState());
		outState.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) mAdapter.getList());
	}


	@Override
	public boolean onSearchRequested() {
		Log.d(TAG, "SearchManager - onSearchRequested: ");
		return super.onSearchRequested();
	}

	private void loadMovieList() {
		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;

				Log.i("Yaeye!", "counting: " + pageCount);
			}
		}
		if (pageCount < PAGE_LIMIT && !loading && ((totalItemCount - visibleItemCount) <=
				  (firstVisibleItem + visibleThreshold))) {
			// End has been reached
			Log.i("Yaeye!", "end called");

			pageCount++;
			binding.progressSearch.setVisibility(View.VISIBLE);
			new SearchMovie().execute(mQuery);

			loading = true;
		}
	}

	@SuppressWarnings("ConstantConditions")
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Log.d(TAG, "Query: " + query);
			//			txt.setText("Searching by: "+ query);
			getSupportActionBar().setTitle(query);

			mQuery = query;
			//			loadMovieList();

		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			String uri = intent.getDataString();
			Log.d(TAG, "Uri: " + uri);
			//			txt.setText("Suggestion: "+ uri);
		}
	}

	private class SearchMovie extends AsyncTask<String, Void, Movies[]> {
		private final String LOG_TAG = SearchMovie.class.getSimpleName();

		@Override
		protected Movies[] doInBackground(String... params) {
			if (params.length == 0)
				return (null);

			// These two need to be declared outside the try/catch
			// so that they can be closed in the finally block.
			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;

			// Will contain the raw JSON response as a string.
			String tmdbJsonStr;

			try {
				final String BASE_URL = "http://api.themoviedb.org/3/search/movie";
				final String QUERY = "query"; // dado recuperado das preferências
				final String PAGE = "page";
				final String APPID_PARAM = "api_key";

				// http://api.themoviedb.org/3/movie/popular?api_key=

				Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PAGE, String
						  .valueOf(page)).appendQueryParameter(QUERY, params[0]).appendQueryParameter
						  (APPID_PARAM, BuildConfig.TMDB_API_KEY).build();

				URL url = new URL(builtUri.toString());

				// Create the request to OpenWeatherMap, and open the connection
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();

				// Read the input stream into a String
				InputStream inputStream = urlConnection.getInputStream();
				if (inputStream == null) {
					// Nothing to do.
					return null;
				}
				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				StringBuilder buffer = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
					// But it does make debugging a *lot* easier if you print out the completed
					// buffer for debugging.
					buffer.append(line).append("\n");
				}

				if (buffer.length() == 0) {
					// Stream was empty.  No point in parsing.
					return (null);
				}
				tmdbJsonStr = buffer.toString();

				return (MoviesDataParser.getMovieDataFromJson(SearchActivity.this, tmdbJsonStr));

			} catch (IOException e) {
				Log.e(LOG_TAG, "Error ", e);
				return (null);
			} catch (JSONException e) {
				Log.e(LOG_TAG, "Error ", e);
				return (null);
			} finally {
				if (urlConnection != null)
					urlConnection.disconnect();

				if (reader != null) {
					try {
						reader.close();

					} catch (final IOException e) {
						Log.e(LOG_TAG, "Error closing stream", e);
					}
				}
			}
		}

		@Override
		protected void onPostExecute(Movies[] movies) {
			if (movies != null) {
				mAdapter.setMovies(page++, Arrays.asList(movies));

				binding.progressSearch.setVisibility(View.GONE);
			}
		}
	}
}