package br.com.mario.popmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/** Created by MarioH on 21/11/2016. */
public class ReviewPageAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Fragment> reviewFragList;

	public ReviewPageAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);

		reviewFragList = list;
	}

	@Override
	public Fragment getItem(int position) {
		return (this.reviewFragList.get(position));
	}

	@Override
	public int getCount() {
		return (this.reviewFragList.size());
	}

	public void setFragmentReviews(ArrayList<Fragment> itens) {
		this.reviewFragList.addAll(itens);
		notifyDataSetChanged();
	}
}