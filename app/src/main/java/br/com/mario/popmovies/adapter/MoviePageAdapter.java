package br.com.mario.popmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Arrays;
import java.util.List;

import br.com.mario.popmovies.PopMoviesApp;
import br.com.mario.popmovies.R;
import br.com.mario.popmovies.frag.TabFragment;

/** Created by MarioH on 08/11/2016. */
public class MoviePageAdapter extends FragmentStatePagerAdapter {
	private final List<Fragment> mFrags;

	public MoviePageAdapter(FragmentManager fm) {
		super(fm);

		Fragment frags[] = new Fragment[]{TabFragment.newInstance("popular"), TabFragment
				  .newInstance("top_rated"), TabFragment.newInstance("favorites")};
		this.mFrags = Arrays.asList(frags);
	}

	@Override
	public Fragment getItem(int position) {
//		switch (position) {
//			case 0:
//				return (TabFragment.newInstance("popular"));
//			case 1:
//				return (TabFragment.newInstance("top_rated"));
//			case 2:
//				return (TabFragment.newInstance("favorites"));
//		}

		return (mFrags.get(position));
	}

	@Override
	public int getCount() {
		return (3);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return (PopMoviesApp.get().getString(R.string.tab1_name));
			case 1:
				return (PopMoviesApp.get().getString(R.string.tab2_name));
			case 2:
				return (PopMoviesApp.get().getString(R.string.tab3_name));
			default:
				return ("NOTHING");
		}
	}
}