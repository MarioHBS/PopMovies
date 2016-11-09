package br.com.mario.popmovies;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Vector;

import br.com.mario.popmovies.frag.TabFragment;

public class MainActivity extends AppCompatActivity {
	private Vector<Fragment> fragments;
	//	private String[] titles;

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

//		titles = new String[2];
//		titles[0] = new String("Page 1");
//		titles[1] = new String("Page 2");

		fragments = new Vector<>();
		fragments.add(TabFragment.newInstance("popular"));
		fragments.add(TabFragment.newInstance("top_rated"));

		PageTabAdapter mPageAdapter = new PageTabAdapter(getSupportFragmentManager(), fragments);
//		ModelPagerAdapter pagerAdapter = new ModelPagerAdapter(getSupportFragmentManager(),
//				  getModelPagerManager());

		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//		viewPager.setOffscreenPageLimit(2); // páginas vizinhas a serem carregadas previamente
//		SpringIndicator indicator = (SpringIndicator) findViewById(R.id.indicator);
//		CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator2);

		viewPager.setAdapter(mPageAdapter);
	}

	/*private PagerManager getModelPagerManager() {
		List<ItemEntity> list = new ArrayList<ItemEntity>();
		for (int i = 0; i < titles.length; i++)
			ItemEntityUtil.create(titles[i]).setModelView(Tab1Fragment.class).attach(list);

		return PagerManager.begin().addFragments(list).setTitles(titles);
	}*/
}