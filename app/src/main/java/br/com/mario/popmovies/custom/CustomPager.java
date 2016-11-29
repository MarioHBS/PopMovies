package br.com.mario.popmovies.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import br.com.mario.popmovies.ReviewPageAdapter;

/**
 * Created by vihaan on 1/9/15.
 */
public class CustomPager extends ViewPager {
	private View mCurrentView;

	public CustomPager(Context context) {
		super(context);
	}

	public CustomPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mCurrentView == null) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}

		int height = 0;

		View child = ((ReviewPageAdapter) getAdapter()).getItem(getCurrentItem()).getView();
		if (child != null) {
			child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec
					  .UNSPECIFIED));
			height = child.getMeasuredHeight();
			if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) && height < getMinimumHeight())
				height = getMinimumHeight();
		}

		// Account for pagerTitleStrip or pagerTabStrip
		View tabStrip = getChildAt(0);
		if (tabStrip instanceof PagerTabStrip) {
			//				tabStrip.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.UNSPECIFIED));
			height += tabStrip.getMeasuredHeight();
		}

		mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		int h = mCurrentView.getMeasuredHeight();
		if (h > height) height = h;
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void measureCurrentView(View currentView) {
		mCurrentView = currentView;
		requestLayout();
	}

	public int measureFragment(View view) {
		if (view == null)
			return 0;

		view.measure(0, 0);
		return view.getMeasuredHeight();
	}
}