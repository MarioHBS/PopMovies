package br.com.mario.popmovies.frag;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.mario.popmovies.R;
import br.com.mario.popmovies.databinding.TabFragReviewBinding;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {
	/** the fragment parameters to the Review */
	private static final String ARG_REVIEW = "review";

	//	@BindView(R.id.review_text)
	//	protected TextView reviewTv;

	private String mReview; // the properly review
	private TabFragReviewBinding binding;

	public ReviewFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @return A new instance of fragment ReviewFragment.
	 */
	public static ReviewFragment newInstance(String param1) {
		ReviewFragment fragment = new ReviewFragment();
		Bundle args = new Bundle();
		args.putString(ARG_REVIEW, param1);
		fragment.setArguments(args);

		return (fragment);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mReview = getArguments().getString(ARG_REVIEW);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
			  savedInstanceState) {
		if (container == null)
			return (null);

		// Inflate the layout for this fragment
		//		View view = inflater.inflate(R.layout.tab_frag_review, container, false);
		//		ButterKnife.bind(this, view);
		binding = DataBindingUtil.inflate(inflater, R.layout.tab_frag_review, container, false);

		setRetainInstance(true);

		return (binding.getRoot());
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (mReview != null)
			binding.reviewTv.setText(mReview);
	}
}