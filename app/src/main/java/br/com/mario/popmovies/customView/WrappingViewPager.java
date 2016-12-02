package br.com.mario.popmovies.customView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import br.com.mario.popmovies.adapter.ReviewPageAdapter;

/**
 * Created by Vihaan Verma on 09/09/2015.
 *
 * Fonte: http://stackoverflow.com/a/32488566
 */
public class WrappingViewPager extends ViewPager {
	private Boolean mAnimStarted = false;

	public WrappingViewPager(Context context) {
		super(context);
	}

	public WrappingViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (!mAnimStarted && null != getAdapter()) {
			int height = 0;
			// TODO Mudança no cast do adapter
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

			// Not the best place to put this animation, but it works pretty good.
			int newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
			if (getLayoutParams().height != 0 && heightMeasureSpec != newHeight) {
				final int targetHeight = height;
				final int currentHeight = getLayoutParams().height;
				final int heightChange = targetHeight - currentHeight;

				Animation a = new Animation() {
					@Override
					public boolean willChangeBounds() {
						return (true);
					}

					@Override
					protected void applyTransformation(float interpolatedTime, Transformation t) {
						if (interpolatedTime >= 1) {
							getLayoutParams().height = targetHeight;
						} else {
							int stepHeight = (int) (heightChange * interpolatedTime);
							getLayoutParams().height = currentHeight + stepHeight;
						}
						requestLayout();
					}
				};

				a.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						mAnimStarted = true;
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						mAnimStarted = false;
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}
				});

				a.setDuration(1000);
				startAnimation(a);
				mAnimStarted = true;
			} else {
				heightMeasureSpec = newHeight;
			}
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}