package com.instirepo.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.instirepo.app.R;
import com.instirepo.app.extras.UserProfileFrameLayout;
import com.instirepo.app.widgets.CircularImageView;

public class ZUserProfileViewedByOtherFragment extends BaseFragment {

	int deviceHeight;
	int initialTranslationOfUserCard, initialTranslationOfScrollView;

	int maxUserCardHeight, minumumUserCardHeight;
	float minTranslationForUserCard;

	CircularImageView circularImageView;
	FrameLayout userCardLayout;
	ScrollView scrollView;
	UserProfileFrameLayout userProfileFrameLayout;

	float downY;

	public static ZUserProfileViewedByOtherFragment newInstance(Bundle b) {
		ZUserProfileViewedByOtherFragment frg = new ZUserProfileViewedByOtherFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.user_profile_viewed_by_other_fragment_layout,
				container, false);

		circularImageView = (CircularImageView) v
				.findViewById(R.id.circularimageview);
		userCardLayout = (FrameLayout) v.findViewById(R.id.usercard);
		scrollView = (ScrollView) v.findViewById(R.id.scrollview);
		userProfileFrameLayout = (UserProfileFrameLayout) v
				.findViewById(R.id.touchinterceptframelayout);

		return v;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						try {
							scrollView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} catch (Exception e) {
							scrollView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}

						performFunctionalityAfterGettingHeightOfComponents();
					}
				});

		userProfileFrameLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.w("as", "on touch");
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					downY = event.getRawY();
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					float deltaY = downY - event.getRawY();
					Log.w("as", "deltay " + deltaY);

					float scrollViewTrans = initialTranslationOfScrollView
							- deltaY;
					if (scrollViewTrans < 0)
						scrollViewTrans = 0;
					scrollView.setTranslationY(scrollViewTrans);

					userCardLayout
							.setTranslationY(getInterpolationForUserCard(deltaY));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {

				}

				return false;
			}
		});
	}

	public float mapValuesFromSet(float oldMin, float oldMax, float newMin,
			float newMax, float valueToChange) {
		float value = (newMax - newMin) * (valueToChange - oldMin)
				/ (oldMax - oldMin) + newMin;
		return value;
	}

	float getInterpolationForUserCard(float deltaY) {
		// float trans = mapValuesFromSet(initialTranslationOfUserCard, 0,
		// initialTranslationOfUserCard, -minTranslationForUserCard,
		// initialTranslationOfUserCard - deltaY);
		float trans = mapValuesFromSet(initialTranslationOfUserCard,
				minTranslationForUserCard, initialTranslationOfUserCard, 0,
				initialTranslationOfUserCard - deltaY);
		Log.w("as", "ini " + initialTranslationOfUserCard + " -  "
				+ (initialTranslationOfUserCard - deltaY) + "  mi  "
				+ minTranslationForUserCard + "  trans  = ==   " + trans);
		if (trans < minTranslationForUserCard)
			trans = minTranslationForUserCard;
		return trans;
	}

	protected void performFunctionalityAfterGettingHeightOfComponents() {
		deviceHeight = userProfileFrameLayout.getHeight();
		initialTranslationOfUserCard = deviceHeight / 4;
		maxUserCardHeight = userCardLayout.getHeight();
		minTranslationForUserCard = 0 - getActivity()
				.getResources()
				.getDimensionPixelSize(R.dimen.z_user_profile_image_height_half);
		minumumUserCardHeight = getActivity().getResources()
				.getDimensionPixelSize(R.dimen.z_toolbar_height);
		initialTranslationOfScrollView = initialTranslationOfUserCard
				+ userCardLayout.getHeight();

		scrollView.setTranslationY(initialTranslationOfScrollView);
		userCardLayout.setTranslationY(initialTranslationOfUserCard);

		circularImageView.setTranslationY(initialTranslationOfUserCard);
	}

}
