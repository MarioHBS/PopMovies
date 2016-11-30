package br.com.mario.popmovies.frag;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;

import br.com.mario.popmovies.BuildConfig;
import br.com.mario.popmovies.R;
import br.com.mario.popmovies.RecyclerAdapter;
import br.com.mario.popmovies.custom.EmptyRecyclerView;
import br.com.mario.popmovies.data.Movies;
import br.com.mario.popmovies.util.GlobalConstants;
import br.com.mario.popmovies.util.ItemDecoration;
import br.com.mario.popmovies.util.MoviesDataParser;
import br.com.mario.popmovies.util.Utilities;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.mario.popmovies.util.GlobalConstants.APPID_PARAM;

/** Created by MarioH on 08/11/2016. */
public class TabFragment extends Fragment {
	private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
	private static final int visibleThreshold = 4;

	/** número de colunas do Grid */
	private final int SPAN_COUNT = 3;
	/** limite do número de páginas carregadas */
	private final int PAGE_LIMIT = 3;

	@BindView(R.id.progress)
	protected ProgressBar mProgress;
	@BindView(R.id.list_grid_movies)
	protected EmptyRecyclerView mRecyclerView;

	private RecyclerAdapter mAdapter;
	private GridLayoutManager mLayoutManager;

	private int firstVisibleItem, visibleItemCount, totalItemCount;
	private int page = 1, pageCount = 1;
	private int previousTotal = 0;

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
		if (container == null)
			return (null);

		View view = inflater.inflate(R.layout.tab_frag_page, container, false);
		ButterKnife.bind(this, view);

		mLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
		mRecyclerView.setLayoutManager(mLayoutManager);

		TextView emptyTv = ButterKnife.findById(view, R.id.empty);
		mRecyclerView.setEmptyView(emptyTv);

		setRetainInstance(true);

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

		if (savedInstanceState == null) {
			if (Utilities.isOnline())
				new GetMovies().execute(mType);
		}
		else {
			mProgress.setVisibility(View.GONE);
			Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable
					  (BUNDLE_RECYCLER_LAYOUT);
			mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
		}
	}

	/**
	 * Called when the fragment's activity has been created and this
	 * fragment's view hierarchy instantiated.  It can be used to do final
	 * initialization once these pieces are in place, such as retrieving
	 * views or restoring state.  It is also useful for fragments that use
	 * {@link #setRetainInstance(boolean)} to retain their instance,
	 * as this callback tells the fragment when it is fully associated with
	 * the new activity instance.  This is called after {@link #onCreateView}
	 * and before {@link #onViewStateRestored(Bundle)}.
	 *
	 * @param savedInstanceState If the fragment is being re-created from
	 *                           a previous saved state, this is the state.
	 */
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mRecyclerView.addItemDecoration(new ItemDecoration(10, SPAN_COUNT));
		mRecyclerView.setAdapter(mAdapter);

		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				if (dy > 0) {
					visibleItemCount = mRecyclerView.getChildCount();
					totalItemCount = mLayoutManager.getItemCount();
					firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

					loadMovieList();
				}
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mLayoutManager.onSaveInstanceState());
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
			// Final da lista foi alcançado

			Log.i("Yaeye!", "end called");

			if (Utilities.isOnline()) {
				pageCount++;
				mProgress.setVisibility(View.VISIBLE);
				new GetMovies().execute(mType);

				loading = true;
			} else
				Toast.makeText(getActivity(), "Não há conexão", Toast.LENGTH_SHORT).show();
		}
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

				Uri builtUri = Uri.parse(BASE_URL).buildUpon()
						  .appendPath(TYPE_PATH)
						  .appendQueryParameter(PAGE, String.valueOf(page))
						  .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY)
						  .build();

				URL url = new URL(builtUri.toString());

				// Create the request to MovieDB, and open the connection
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setConnectTimeout(GlobalConstants.CONNECTION_TIME_LIMIT);
				urlConnection.setReadTimeout(GlobalConstants.CONNECTION_TIME_LIMIT);
				urlConnection.connect();

				// Read the input stream into a String
				InputStream inputStream = urlConnection.getInputStream();
				if (inputStream == null)
					return (null);

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

				tmdbJsonStr = buffer.toString();

				return (MoviesDataParser.getMovieDataFromJson(getContext(), tmdbJsonStr));

			} catch (SocketTimeoutException ste) {
				Log.e(LOG_TAG, "Time to connect exprired: Loading isn't completed", ste);
				// TODO tratar a não conexão
				return (null);
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