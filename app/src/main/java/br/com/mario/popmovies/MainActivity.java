package br.com.mario.popmovies;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.StackTransformer;

import java.util.Vector;

import br.com.mario.popmovies.frag.TabFragment;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String KEY_SEARCH_QUERY = "searchQuery";

	private Vector<Fragment> fragments;

	private String currentQuery;
	private SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setLogo(R.drawable.film_reel);
		getSupportActionBar().setDisplayUseLogoEnabled(true);

		// configuração de política de Thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		fragments = new Vector<>();
		fragments.add(TabFragment.newInstance("popular"));
		fragments.add(TabFragment.newInstance("top_rated"));

		PageTabAdapter mPageAdapter = new PageTabAdapter(getSupportFragmentManager(), fragments);

		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		//		viewPager.setOffscreenPageLimit(2); // páginas vizinhas a serem carregadas previamente
		//		SpringIndicator indicator = (SpringIndicator) findViewById(R.id.indicator);

		viewPager.setAdapter(mPageAdapter);
		viewPager.setPageTransformer(true, new StackTransformer());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		//Save the fragment's instance
//		getSupportFragmentManager().putFragment(outState, "frag1", fragments.get(0));
//		getSupportFragmentManager().putFragment(outState, "frag2", fragments.get(1));

		String ss = searchView.getQuery().toString();
		outState.putString(KEY_SEARCH_QUERY, ss);

		Log.i("FragLife", "Activity: onSaveInstanceState: " + outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		Log.i("FragLife", "Activity: onCreate: " + savedInstanceState);
		if (savedInstanceState != null) {
			currentQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
//
//			fragments.setElementAt(getSupportFragmentManager().getFragment(savedInstanceState, "frag1"), 0);
//			fragments.setElementAt(getSupportFragmentManager().getFragment(savedInstanceState, "frag2"), 1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.menu_search);
		searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setOnQueryTextListener(this);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this,
				  SearchActivity.class)));
//		searchItem.expandActionView();
//		searchView.setQuery("aaaa", false);

		if (!TextUtils.isEmpty(currentQuery)) {
			searchItem.expandActionView();
			searchView.setQuery(currentQuery, false);
//			searchView.clearFocus();
		}

//		searchView.setIconifiedByDefault(false);

		return (true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will automatically handle clicks on the
		// Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();

//		if (id == R.id.menu_search) {
//
//		}

		return (super.onOptionsItemSelected(item));
	}

	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();

		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			String uri = intent.getDataString();
			Toast.makeText(this, "Suggestion: " + uri, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Called when the user submits the query. This could be due to a key press on the
	 * keyboard or due to pressing a submit button.
	 * The listener can override the standard behavior by returning true
	 * to indicate that it has handled the submit request. Otherwise return false to
	 * let the SearchView handle the submission by launching any associated intent.
	 *
	 * @param query the query text that is to be submitted
	 * @return true if the query has been handled by the listener, false to let the
	 * SearchView perform the default action.
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		Log.d(TAG, "onQueryTextSubmit: " + query);
		return (false);
	}

	/**
	 * Called when the query text is changed by the user.
	 *
	 * @param newText the new content of the query text field.
	 * @return false if the SearchView should perform the default action of showing any
	 * suggestions if available, true if the action was handled by the listener.
	 */
	@Override
	public boolean onQueryTextChange(String newText) {
//		currentQuery = newText;
		return (false);
	}
}

// http://stackoverflow.com/questions/22498344/is-there-a-better-way-to-restore-searchview-state