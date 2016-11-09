package br.com.mario.popmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;
import java.util.Vector;

import br.com.mario.popmovies.frag.TabFragment;

/** Created by MarioH on 08/11/2016. */
public class PageTabAdapter extends FragmentPagerAdapter {
	private final List<Fragment> mFrags;

	public PageTabAdapter(FragmentManager fm, Vector<Fragment> fragments) {
		super(fm);

		this.mFrags = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		/*switch (position) {
			case 0:
				return (TabFragment.newInstance("popular"));
			case 1:
				return (TabFragment.newInstance("top_rated"));
		}*/

		return (mFrags.get(position));
	}

	@Override
	public int getCount() {
		return (2);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return (PopMoviesApplication.CTX.getString(R.string.tab1_name));
			case 1:
				return (PopMoviesApplication.CTX.getString(R.string.tab2_name));
			default:
				return ("NOTHING");
		}
	}
}