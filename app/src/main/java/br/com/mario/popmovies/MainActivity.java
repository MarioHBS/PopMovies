package br.com.mario.popmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import br.com.mario.popmovies.data.Movies;
import br.com.mario.popmovies.util.MoviesDataParser;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
	@BindView(R.id.list_grid_movies)
	protected RecyclerView listView;

	private RecyclerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		mAdapter = RecyclerAdapter.getInstance();

		listView.setLayoutManager(new GridLayoutManager(this, 3));
		listView.setAdapter(mAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new GetMovies().execute("popular");
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
				final String APPID_PARAM = "api_key";

				// http://api.themoviedb.org/3/movie/popular?api_key=080ed140011f67b8a97c64028cb587b4

				Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(TYPE_PATH)
						  .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY).build();
				// TODO terminar a construção da URI

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

				return (MoviesDataParser.getMovieDataFromJson(tmdbJsonStr));

			} catch (IOException e) {
				Log.e(LOG_TAG, "Error ", e);
				return (null);
			} catch (JSONException e) {
				Log.e(LOG_TAG, "Error ", e);
				return (null);
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
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
			if (movies != null)
				mAdapter.setMovies(Arrays.asList(movies));
		}
	}
}