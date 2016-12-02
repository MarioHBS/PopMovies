package br.com.mario.popmovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;
import java.util.Vector;

import br.com.mario.popmovies.PopMoviesApplication;
import br.com.mario.popmovies.R;
import br.com.mario.popmovies.frag.TabFragment;

/** Created by MarioH on 08/11/2016. */
public class MoviePageAdapter extends FragmentStatePagerAdapter {
//	private final List<Fragment> mFrags;

	public MoviePageAdapter(FragmentManager fm) {
		super(fm);

//		this.mFrags = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return (TabFragment.newInstance("popular"));
			case 1:
				return (TabFragment.newInstance("top_rated"));
		}

		return (null);
	}

	@Override
	public int getCount() {
		return (2);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return (PopMoviesApplication.getInstance().getString(R.string.tab1_name));
			case 1:
				return (PopMoviesApplication.CTX.getString(R.string.tab2_name));
			default:
				return ("NOTHING");
		}
	}
}