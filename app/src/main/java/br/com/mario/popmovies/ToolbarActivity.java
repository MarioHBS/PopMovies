package br.com.mario.popmovies;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import br.com.mario.popmovies.frag.TabFragment;

import static br.com.mario.popmovies.R.id.bottomBar;

public class ToolbarActivity extends AppCompatActivity {
	//indices to fragments
	private final int TAB_FIRST = FragNavController.TAB1;
	private final int TAB_SECOND = FragNavController.TAB2;
	private final int TAB_THIRD = FragNavController.TAB3;
	private BottomBar mBottomBar;
	private FragNavController fragNavController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toolbar);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		List<Fragment> fragments = new ArrayList<>(3);

		// configuração de política de Thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		//add fragments to list
		fragments.add(TabFragment.newInstance("popular"));
		fragments.add(TabFragment.newInstance("top_rated"));
		fragments.add(TabFragment.newInstance("popular"));

		//link fragments to container
		fragNavController = new FragNavController(savedInstanceState, getSupportFragmentManager(), R
				  .id.container, fragments, 0);
		//End of FragNav

		mBottomBar = (BottomBar) findViewById(bottomBar);

		mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelected(@IdRes int tabId) {
				//				Log.d(TAG, "onTabSelected: some Tab selected");

				switch (tabId) {
					case R.id.tab_pop:
						//						Log.d(TAG, "onTabSelected: Populares");
						fragNavController.switchTab(TAB_FIRST);
						break;
					case R.id.tab_most:
						Toast.makeText(ToolbarActivity.this, "Melhores selecionado", Toast.LENGTH_SHORT)
								  .show();
						fragNavController.switchTab(TAB_SECOND);
						break;
					case R.id.tab_fav:
						fragNavController.switchTab(TAB_THIRD);
						break;
				}
			}
		});

		mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
			@Override
			public void onTabReSelected(@IdRes int tabId) {
				if (tabId == R.id.tab_pop) {
					Toast.makeText(ToolbarActivity.this, "Popular Reselecionado", Toast.LENGTH_SHORT)
							  .show();
					fragNavController.clearStack();
				}
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (fragNavController != null) {
			fragNavController.onSaveInstanceState(outState);
		}
	}

	@Override
	public void onBackPressed() {
		if (fragNavController.getCurrentStack().size() > 1) {
			fragNavController.pop();
		} else
			super.onBackPressed();
	}
}