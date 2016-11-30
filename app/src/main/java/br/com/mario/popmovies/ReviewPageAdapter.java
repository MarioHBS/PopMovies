package br.com.mario.popmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.mario.popmovies.custom.CustomPager;

/** Created by MarioH on 21/11/2016. */
public class ReviewPageAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Fragment> reviewFragList;
	private int mCurrentPosition = -1;

	public ReviewPageAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);

		reviewFragList = list;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);

		if (position != mCurrentPosition) {
			Fragment fragment = (Fragment) object;
			CustomPager pager = (CustomPager) container;
			if (fragment != null && fragment.getView() != null) {
				mCurrentPosition = position;
				pager.measureCurrentView(fragment.getView());
			}
		}
	}

	@Override
	public Fragment getItem(int position) {
		return (this.reviewFragList.get(position));
	}

	@Override
	public int getCount() {
		return (this.reviewFragList.size());
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return (String.valueOf(position + 1));
	}
}