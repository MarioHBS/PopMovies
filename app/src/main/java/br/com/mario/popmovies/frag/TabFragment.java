package br.com.mario.popmovies.frag;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import br.com.mario.popmovies.BuildConfig;
import br.com.mario.popmovies.R;
import br.com.mario.popmovies.RecyclerAdapter;
import br.com.mario.popmovies.data.Movies;
import br.com.mario.popmovies.util.ItemDecoration;
import br.com.mario.popmovies.util.MoviesDataParser;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Created by MarioH on 08/11/2016. */
public class TabFragment extends Fragment {
	@BindView(R.id.list_grid_movies)
	protected RecyclerView mRecyclerView;
	@BindView(R.id.progress)
	protected ProgressBar mProgress;

	int pastVisiblesItems, visibleItemCount, totalItemCount;
	int page = 1;
	private RecyclerAdapter mAdapter;
	private boolean loading = true;
	private String mType;

	// newInstance constructor for creating fragment with arguments
	public static TabFragment newInstance(String type) {
		TabFragment fragment = new TabFragment();
		Bundle args = new Bundle();
		args.putString("movietype", type);
		fragment.setArguments(args);

		return (fragment);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mType = getArguments().getString("movietype");

		mAdapter = RecyclerAdapter.getInstance();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
			  Bundle savedInstanceState) {
		if (container == null) {
			return (null);
		}

		View view = inflater.inflate(R.layout.tab_frag_layout, container, false);
		ButterKnife.bind(this, view);

		return (view);
	}

	/**
	 * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
	 * has returned, but before any saved state has been restored in to the view.
	 * This gives subclasses a chance to initialize themselves once
	 * they know their view hierarchy has been completely created.  The fragment's
	 * view hierarchy is not however attached to its parent at this point.
	 *
	 * @param view               The View returned by
	 *                           {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
	 * @param savedInstanceState If non-null, this fragment is being re-constructed
	 */
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.addItemDecoration(new ItemDecoration(10, 2));
		mRecyclerView.setAdapter(mAdapter);

		mRecyclerView.setVisibility(View.GONE);

		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				if (dy > 0) {
					visibleItemCount = mLayoutManager.getChildCount();
					totalItemCount = mLayoutManager.getItemCount();
					pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

					if (loading) {
						if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
							loading = false;
							Log.v("...", "Last Item Wow !");

							new GetMovies().execute("popular");
							//Do pagination.. i.e. fetch new data
						}
					}
				}
			}
		});
	}

	/**
	 * Called when the Fragment is visible to the user.  This is generally
	 * tied to {@link Activity#onStart() Activity.onStart} of the containing
	 * Activity's lifecycle.
	 */
	@Override
	public void onStart() {
		super.onStart();
		new GetMovies().execute(mType);
	}

	private class GetMovies extends AsyncTask<String, Void, Movies[]> {
		private final String LOG_TAG = GetMovies.class.getSimpleName();

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
				final String BASE_URL = "http://api.themoviedb.org/3/movie";
				final String TYPE_PATH = params[0]; // dado recuperado das preferências
				final String PAGE = "page";
				final String APPID_PARAM = "api_key";

				// http://api.themoviedb.org/3/movie/popular?api_key=080ed140011f67b8a97c64028cb587b4

				Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(TYPE_PATH)
						  .appendQueryParameter(PAGE, String.valueOf(page)).appendQueryParameter
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

				return (MoviesDataParser.getMovieDataFromJson(getContext(), tmdbJsonStr));

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

				mRecyclerView.setVisibility(View.VISIBLE);
				mProgress.setVisibility(View.GONE);
			}
		}
	}
}