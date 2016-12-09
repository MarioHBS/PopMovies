package br.com.mario.popmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.bottombar.BottomBar;

/**
 * A placeholder fragment containing a simple view.
 */
public class ToolbarActivityFragment extends Fragment {
	private static final String TAG = ToolbarActivityFragment.class.getSimpleName();
	BottomBar bottomBar;

	public ToolbarActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
			  savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_toolbar, container, false);

//		bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//			@Override
//			public void onTabSelected(@IdRes int tabId) {
//				Log.d(TAG, "onTabSelected: some Tab selected");
//
//				switch (tabId) {
//					case R.id.tab_pop:
//						Log.d(TAG, "onTabSelected: Populares");
//						break;
//					case R.id.tab_most:
//						Toast.makeText(getActivity(), "Melhores selecionado", Toast.LENGTH_SHORT).show();
//						break;
//					case R.id.tab_fav:
//
//						break;
//				}
//			}
//		});

//		BottomBarTab barTab = bottomBar.getTabWithId(R.id.tab_pop);
//		barTab.setBadgeCount(4);

		return (view);
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

	}
}